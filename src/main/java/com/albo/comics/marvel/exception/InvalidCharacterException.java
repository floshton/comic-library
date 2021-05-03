package com.albo.comics.marvel.exception;

import java.io.Serializable;

public class InvalidCharacterException extends Exception implements Serializable {
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