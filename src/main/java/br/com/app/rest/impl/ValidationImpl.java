package br.com.app.rest.impl;

import br.com.app.model.B64Token;
import br.com.app.rest.Validation;

import javax.ws.rs.core.Response;

public class ValidationImpl implements Validation {

    @Override
    public Response validate(B64Token token) {
        return Response.ok().build();
    }
}
