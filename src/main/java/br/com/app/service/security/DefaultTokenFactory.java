package br.com.app.service.security;

import br.com.app.model.Credentials;
import br.com.app.model.JWT;

public class DefaultTokenFactory extends TokenFactory {

    @Override
    public JWT issueToken(Credentials credentials) {
        return null;
    }
}
