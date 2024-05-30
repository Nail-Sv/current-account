package com.currentaccount.currentaccount.controller;

import com.currentaccount.currentaccount.dto.AccountDTO;
import com.currentaccount.currentaccount.dto.ErrorGroupDTO;
import com.currentaccount.currentaccount.dto.InitialRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Account")
@RequestMapping(value = AccountController.REST_URL)
public interface AccountController {

    String REST_URL = "/api/v1/accounts";

    @Operation(
            summary = "Creates a new account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Returns DTO of the newly created account with new id assigned and Location header.",
                            content = @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE),
                            headers = @Header(name = "Location", description = "URL of the newly created account", schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "400", description = "Invalid Request Body (Some validation failed)",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Customer not found",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "415", description = "Media Type Not Supported",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "500", description = "Server error",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<AccountDTO> createAccount(@RequestBody InitialRequestDTO initialRequestDTO);

}
