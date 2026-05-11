package com.retailer.rewards.exception;

public class InvalidTransactionAmountException extends RuntimeException {

    public InvalidTransactionAmountException(double amount) {
        super("Transaction amount must be non-negative, but was: " + amount);
    }
}
