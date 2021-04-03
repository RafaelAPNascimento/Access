package br.com.app.integration;

import br.com.app.integration.annotations.IntegrationTest;
import br.com.app.integration.util.Constants;
import br.com.app.model.Credentials;
import br.com.app.model.JWT;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static br.com.app.integration.util.Constants.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTest {

    private JWT jwt;

    @BeforeAll
    public void init() throws ClassNotFoundException {

        Class.forName(ConfigCertificate.class.getName());

    }

    @IntegrationTest
    public void shouldRejectByEmptyCredentials() {

        String path = "/connect/token";

            given().baseUri(BASE_URI)
                    .basePath(path)
                    .contentType(ContentType.JSON)
                    .request()
                    .log().all()
                    .when().post()
                    .then().assertThat().statusCode(SC_BAD_REQUEST)
                    .log().all();
    }

    @IntegrationTest
    public void shouldRejectByInvalidCredentials() {

        String path = "/connect/token";

        given().baseUri(BASE_URI)
                .basePath(path)
                .contentType(ContentType.JSON)
                .request()
                .body(INVALID_CREDENTIALS)
                .log().all()
                .when().post()
                .then().assertThat().statusCode(SC_FORBIDDEN)
                .log().all();
    }

    @IntegrationTest
    public void shouldAuthenticate() {

        String path = "/connect/token";

        Response response =
                given().baseUri(BASE_URI)
                        .basePath(path)
                        .contentType(ContentType.JSON)
                        .request()
                        .body(CREDENTIALS)
                        .log().all()
                        .when().post()
                        .then().log().all()
                        .extract().response() ;

        jwt = response.jsonPath().getObject("$", JWT.class);
        Assertions.assertNotNull(jwt);
    }
}
