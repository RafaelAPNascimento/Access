package br.com.app.integration.util;

import br.com.app.model.Credentials;

public class Constants {

    public static final String BASE_URI = "https://localhost:8443/access/api";
    public static final Credentials CREDENTIALS = new Credentials("78910", "read_only");
    public static final Credentials INVALID_CREDENTIALS = new Credentials("KKKKK", "KKKKKK");

}
