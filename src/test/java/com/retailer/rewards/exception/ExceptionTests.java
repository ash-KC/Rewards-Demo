package com.retailer.rewards.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTests {

    @Test
    @DisplayName("CustomerNotFoundException contains customer ID in message")
    void customerNotFoundException_containsId() {
        CustomerNotFoundException exception = new CustomerNotFoundException(42L);
        assertTrue(exception.getMessage().contains("42"));
    }

    @Test
    @DisplayName("InvalidTransactionAmountException contains amount in message")
    void invalidTransactionAmountException_containsAmount() {
        InvalidTransactionAmountException exception = new InvalidTransactionAmountException(-15.5);
        assertTrue(exception.getMessage().contains("-15.5"));
    }
}
