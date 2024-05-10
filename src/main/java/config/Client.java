package config;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static config.Constants.*;
import static io.restassured.RestAssured.given;

public class Client {

    public RequestSpecification spec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                ;
    }
}