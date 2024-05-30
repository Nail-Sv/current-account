package com.currentaccount.currentaccount.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

/**
 * A Data Transfer Object that represents a customer.
 */
@Schema(description = "A Data Transfer Object that represents a customer.")
public record CustomerDTO(

        @Schema(description = "The unique identifier of the customer.", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @NotBlank
        @Schema(description = "The first name of the customer. Must not be blank.", example = "John")
        String name,

        @NotBlank
        @Schema(description = "The last name of the customer. Must not be blank.", example = "Doe")
        String surname,

        @Schema(description = "The current balance of the customer.", example = "1000.00")
        double balance,

        @Schema(description = "The list of accounts associated with the customer.")
        List<AccountDTO> accounts

) {}
