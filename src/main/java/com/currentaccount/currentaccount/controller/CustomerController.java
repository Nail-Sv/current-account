package com.currentaccount.currentaccount.controller;

import com.currentaccount.currentaccount.dto.CustomerDTO;
import com.currentaccount.currentaccount.dto.ErrorGroupDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customer")
@RequestMapping(value = CustomerController.REST_URL)
public interface CustomerController {

    String REST_URL = "/api/v1/customers";

    @Operation(
            summary = "Returns a customer by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns a customer by its id",
                            content = @Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Customer not found",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "500", description = "Server error",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            },
            parameters = {
                    @Parameter(
                            name = "customerId",
                            description = "The UUID of the customer to retrieve",
                            required = true,
                            example = "550e8400-e29b-41d4-a716-446655440000")
            })
    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") UUID customerId);

    @Operation(
            summary = "Returns all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns all customers",
                            content= @Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "500", description = "Server error",
                            content = @Content(schema = @Schema(implementation = ErrorGroupDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @GetMapping(value ="/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    ResponseEntity<List<CustomerDTO>> getCustomers();
}
