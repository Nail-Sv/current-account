package com.currentaccount.currentaccount.controller;

import com.currentaccount.currentaccount.dto.ErrorDTO;
import com.currentaccount.currentaccount.dto.ErrorGroupDTO;
import com.currentaccount.currentaccount.exception.NotFoundException;
import com.currentaccount.currentaccount.exception.ValidationException;
import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionController {

    private static final String UNKNOWN = "unknown";
    private final Tracer tracer;

    // when client requested media type that is not supported, e.g. raw
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorGroupDTO> handleHttpMediaTypeNotSupportedException(
            @NonNull WebRequest request,
            @NonNull HttpMediaTypeNotSupportedException e) {

        logError(request, e);

        StringBuilder message = new StringBuilder();
        message.append(e.getContentType());
        message.append(" media type is not supported. Supported media types are: ");

        e.getSupportedMediaTypes().forEach(t -> message.append(t).append(" "));

        ErrorDTO errorDTO = new ErrorDTO(message.toString(), null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(List.of(errorDTO)), UNSUPPORTED_MEDIA_TYPE);
    }

    // when client requested media type that is not supported, e.g. application/xml
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorGroupDTO> handleHttpMediaTypeNotAcceptableException(
            @NonNull WebRequest request,
            @NonNull HttpMediaTypeNotAcceptableException e) {

        logError(request, e);

        StringBuilder message = new StringBuilder();
        message.append(e.getSupportedMediaTypes());
        message.append(" media type is not supported. Supported media types are: ");

        e.getSupportedMediaTypes().forEach(t -> message.append(t).append(" "));

        ErrorDTO errorDTO = new ErrorDTO(message.toString(), null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(List.of(errorDTO)), UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorGroupDTO> handleValidationException(
            @NonNull WebRequest request,
            @NonNull ValidationException e) {

        logError(request, e);

        ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(Collections.singletonList(errorDTO)), BAD_REQUEST);
    }

    //when customer does not exist
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorGroupDTO> handleNotFoundException(
            @NonNull WebRequest request,
            @NonNull NotFoundException e) {

        logError(request, e);

        ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(Collections.singletonList(errorDTO)), NOT_FOUND);
    }

    // when client requested URI that does not exist
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorGroupDTO> handleNoHandlerFoundException(
            @NonNull WebRequest request,
            @NonNull NoResourceFoundException e) {

        logError(request, e);

        ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(Collections.singletonList(errorDTO)), BAD_REQUEST);
    }

    // when client provided invalid data in request path, e.g. /api/v1/customers/123 instead of /api/v1/customers/uuid
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorGroupDTO> handleMethodArgumentTypeMismatchException(
            @NonNull WebRequest request,
            @NonNull MethodArgumentTypeMismatchException e) {

        logError(request, e);

        String type = e.getRequiredType() != null ? e.getRequiredType().getName() : UNKNOWN;
        String message = String.format("%s should be of type %s", e.getName(), type);

        ErrorDTO errorDTO = new ErrorDTO(message, null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(Collections.singletonList(errorDTO)), BAD_REQUEST);
    }

    // ideally, this should never happen
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorGroupDTO> handleUnknownException(@NonNull WebRequest request, @NonNull Exception e) {

        logError(request, e);

        // we do not want to expose internal error messages to the client
        ErrorDTO errorDTO = new ErrorDTO(UNKNOWN, null, null, getTraceId().orElse(null));

        return buildErrorResponseEntity(new ErrorGroupDTO(List.of(errorDTO)), INTERNAL_SERVER_ERROR);
    }


    private ErrorDTO buildErrorDTO(@NonNull ConstraintViolation<?> violation) {
        return new ErrorDTO(
                violation.getMessage(),
                Optional.ofNullable(violation.getInvalidValue()).map(Object::toString).orElse(null),
                Optional.ofNullable(violation.getPropertyPath()).map(Path::toString).orElse(null),
                getTraceId().orElse(null));
    }

    private void logError(@NonNull WebRequest request, @NonNull Exception e) {
        log.error(String.format("Error at request: %s", request.getDescription(true)), ExceptionUtils.getRootCause(e));
    }

    private ResponseEntity<ErrorGroupDTO> buildErrorResponseEntity(
            @NonNull ErrorGroupDTO errorGroupDTO,
            @NonNull HttpStatus httpStatus) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorGroupDTO, headers, httpStatus);
    }

    private Optional<String> getTraceId() {
        return Optional.ofNullable(tracer)
                .map(Tracer::currentTraceContext)
                .map(CurrentTraceContext::context)
                .map(TraceContext::traceId);
    }
}
