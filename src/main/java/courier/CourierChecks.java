package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;

public class CourierChecks {
    @Step("Успешный  логин курьера")
    public int loggedInSuccessfully(ValidatableResponse loginResponse) {
        int id = loginResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("id")
                ;
        return id;
    }

    @Step("Успешное создание курьера")
    public void createdSuccessfully(ValidatableResponse createResponse) {
        boolean created = createResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .extract()
                .path("ok")
                ;
        assertTrue(created);
    }

    @Step("Успешное удаление курьера")
    public void deletedSuccesfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(HTTP_OK)
                .body("ok", is(true))
        ;
    }
}