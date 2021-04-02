package br.com.app.integration;

import br.com.app.integration.annotations.IntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static br.com.app.integration.util.Constants.BASE_URI;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTest {

    @BeforeAll
    public void init() throws ClassNotFoundException {
        Class.forName(ConfigCertificate.class.getName());
    }

    @IntegrationTest
    public void shouldAuthenticate() throws Exception {

        String path = "/connect/validate";

        given().baseUri(BASE_URI)
                .basePath(path)
                .contentType(ContentType.JSON)
                .request()
                .body("{\"token\":\"aaaa\"}")
                .log().all()
                .when().post()
                .then().log().all()
                .assertThat().statusCode(SC_OK);

    }

}
