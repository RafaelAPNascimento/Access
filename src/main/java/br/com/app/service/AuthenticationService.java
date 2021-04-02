package br.com.app.service;

import br.com.app.model.Credentials;

public interface AuthenticationService {

    boolean authenticate(Credentials credentials);
}
