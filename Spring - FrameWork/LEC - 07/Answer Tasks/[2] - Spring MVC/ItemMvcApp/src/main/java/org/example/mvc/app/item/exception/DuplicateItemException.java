package org.example.mvc.app.item.exception;

public class DuplicateItemException extends RuntimeException {

    public DuplicateItemException(String message) {
        super(message);
    }
}