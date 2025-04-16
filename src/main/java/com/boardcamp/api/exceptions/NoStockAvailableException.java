package com.boardcamp.api.exceptions;

public class NoStockAvailableException extends RuntimeException {
    public NoStockAvailableException(String message) {
        super(message);
    }
}
