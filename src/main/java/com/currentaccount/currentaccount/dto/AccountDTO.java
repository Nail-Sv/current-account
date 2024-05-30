package com.currentaccount.currentaccount.dto;

import java.util.List;
import java.util.UUID;

public record AccountDTO(

        UUID id,

        double balance,

        List<TransactionDTO> transactions
) {}
