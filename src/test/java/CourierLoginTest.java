import courier.Courier;
import courier.CourierChecks;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static Config.Constants.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotEquals;

public class CourierLoginTest {
    private final CourierClient client = new CourierClient();
    private final CourierChecks check = new CourierChecks();
    int courierId;

    @After
    @Description("Удаление курьера")
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccesfully(response);
        }
    }

    @Test
    @DisplayName("Авторизация курьера с корректными данными")
    public void courierLoginSuccess() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);

        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Авторизация курьера без поля login")
    public void courierLoginNull() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        creds.setLogin(null);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        loginResponse.statusCode(HTTP_BAD_REQUEST)
                .body("message", containsString(NEGATIVE_AUTH_MESSAGE));
    }

    @Test
    @DisplayName("Авторизация курьера без поля password")
    public void courierPasswordNull() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        creds.setPassword(null);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        loginResponse.statusCode(HTTP_BAD_REQUEST)
                .body("message", containsString(NEGATIVE_AUTH_MESSAGE));

    }

    @Test
    @DisplayName("Авторизация курьера c не корретным паролем")
    public void courierIncorrectPassword() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        creds.setPassword(WRONG_PASSWORD);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        loginResponse.statusCode(HTTP_NOT_FOUND)
                .body("message", containsString(NON_EXIST_MASSAGE));

    }
    @Test
    @DisplayName("Авторизация курьера с не корректным логином")
    public void courierIncorrectLogin(){
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        creds.setLogin(WRONG_LOGIN);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        loginResponse.statusCode(HTTP_NOT_FOUND)
                .body("message", containsString(NON_EXIST_MASSAGE));
    }

}
