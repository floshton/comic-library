package com.albo.comics.marvel.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidCharacterExceptionHandler implements ExceptionMapper<InvalidCharacterException> {
    @Override
    public Response toResponse(InvalidCharacterException exception) {
        return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).type("application/json").build();
    }
}