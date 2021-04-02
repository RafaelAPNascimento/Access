package br.com.app.service.security;

import br.com.app.model.Credentials;
import br.com.app.model.JWT;

public abstract class TokenFactory {

    public abstract JWT issueToken(Credentials credentials);
}
