package br.com.app.rest.impl;

import br.com.app.model.Credentials;
import br.com.app.model.JWT;
import br.com.app.rest.Authentication;
import br.com.app.service.AuthenticationService;
import br.com.app.service.security.TokenFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

public class AuthenticationImpl implements Authentication {

    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private TokenFactory tokenFactory;

    @Override
    public Response sentPayload(@NotNull Credentials credentials) {

        try {
            proceedAuthentication(credentials);
            JWT jwt = tokenFactory.issueToken(credentials);
            return Response.ok(jwt).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private void proceedAuthentication(Credentials credentials) {

        boolean authenticated = authenticationService.authenticate(credentials);
        if (!authenticated)
            throw new IllegalArgumentException("Invalid Credentials");
    }
}
