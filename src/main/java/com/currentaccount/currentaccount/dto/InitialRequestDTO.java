package com.currentaccount.currentaccount.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;

import java.util.UUID;

public record InitialRequestDTO(

        @Schema(
                description = "The unique identifier of the customer",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID customerId,

        @Schema(
                description = "The initial credit amount for the new account",
                example = "150.0"
        )
        @Digits(integer = 3, fraction = 2)
        double initialCredit
) {
}
