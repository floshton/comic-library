package com.albo.comics.marvel.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoDataAvailableExceptionHandler implements ExceptionMapper<NoDataAvailableException> {
    @Override
    public Response toResponse(NoDataAvailableException exception) {
        return Response.status(Status.NOT_FOUND).entity(exception.getMessage()).type("application/json").build();
    }
}