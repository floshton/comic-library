package com.albo.comics.marvel.exception;

public class InvalidCharacterException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidCharacterException() {
        super();
    }

    public InvalidCharacterException(String msg) {
        super(msg);
    }

    public InvalidCharacterException(String msg, Exception e) {
        super(msg, e);
    }
}