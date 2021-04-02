package br.com.app.integration;

import br.com.app.integration.annotations.IntegrationTest;
import br.com.app.model.Credentials;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static br.com.app.integration.util.Constants.BASE_URI;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTest {

    private static Credentials credentials;

    @BeforeAll
    public void init() throws ClassNotFoundException {

        Class.forName(ConfigCertificate.class.getName());
        credentials = new Credentials("78910", "read_only");
    }

    @IntegrationTest
    public void shouldAuthenticate() throws Exception {

        String path = "/connect/token";

        given().baseUri(BASE_URI)
                        .basePath(path)
                        .contentType(ContentType.JSON)
                        .request()
                        .body(credentials)
                        .log().all()
                        .when().post()
                        .then().log().all()
                        .assertThat().statusCode(SC_OK);

    }
}
