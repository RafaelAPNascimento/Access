package br.com.app.service.security;

import br.com.app.model.Credentials;
import br.com.app.model.JWT;

public abstract class TokenFactory {

    protected final static String ISSUER = "rafael.senior.engineer";
    protected final static String HEADER = "{\"alg\":\"HS256\", \"typ\":\"jwt\"}";

    public abstract JWT issueToken(Credentials credentials);
}
