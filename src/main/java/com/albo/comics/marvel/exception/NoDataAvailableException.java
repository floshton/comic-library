package com.albo.comics.marvel.exception;

public class NoDataAvailableException extends Exception {
    private static final long serialVersionUID = 1L;

    public NoDataAvailableException() {
        super();
    }

    public NoDataAvailableException(String msg) {
        super(msg);
    }

    public NoDataAvailableException(String msg, Exception e) {
        super(msg, e);
    }
}