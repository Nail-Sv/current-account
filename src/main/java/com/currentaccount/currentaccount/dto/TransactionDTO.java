package com.currentaccount.currentaccount.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDTO(

        UUID id,

        double amount,

        LocalDateTime created_at

) {}
