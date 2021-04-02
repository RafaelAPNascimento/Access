package br.com.app.rest;

import br.com.app.rest.impl.AuthenticationImpl;
import br.com.app.rest.impl.ValidationImpl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(ValidationImpl.class);
        resources.add(AuthenticationImpl.class);

        return resources;
    }
}
