package com.albo.comics.marvel.exception;

import java.io.Serializable;

public class NoDataAvailableException extends Exception implements Serializable {
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