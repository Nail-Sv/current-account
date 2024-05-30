package com.currentaccount.currentaccount.model;

import java.util.UUID;

public record InitialRequest(

        UUID customerId,

        double initialCredit
) {
}
