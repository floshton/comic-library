package com.albo.comics.marvel.exception;

import java.io.Serializable;

public class ApiSyncException extends Exception implements Serializable {
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