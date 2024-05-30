package com.currentaccount.currentaccount.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Error")
@SuppressWarnings("unused")
public record ErrorDTO(
        @Schema(description = "Error message.", example = "This is error message")
        String message,
        @Schema(description = "Value that failed validation. It is present in case of validation errors.", example = "DE12345", nullable = true)
        String failedValue,
        @Schema(description = "Path of the field that did not pass validation. It is present in case of validation errors.", example = "contactEmail", nullable = true)
        String path,
        @Schema(description = "Trace ID that can be used to see all the logs associated with a single client request.",
                example = "2514c7ea-284a-4126-a6f2-5a434e4cfde4")
        String traceId
) {}
