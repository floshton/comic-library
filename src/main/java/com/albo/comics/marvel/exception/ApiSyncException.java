package com.albo.comics.marvel.exception;

public class ApiSyncException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApiSyncException() {
        super();
    }

    public ApiSyncException(String msg) {
        super(msg);
    }

    public ApiSyncException(String msg, Exception e) {
        super(msg, e);
    }
}