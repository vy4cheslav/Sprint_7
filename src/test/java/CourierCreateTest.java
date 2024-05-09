import courier.Courier;
import courier.CourierChecks;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import java.net.HttpURLConnection;

import static Config.Constants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;

public class CourierCreateTest {

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
    @DisplayName("Создание курьера с корректными данными")
    public void courierCreate() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);

        assertNotEquals(0, courierId);
    }
    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void canNotCreateTwoIdenticalCouriersTest() {
        Courier existingCourier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(existingCourier);
        check.createdSuccessfully(createResponse);

        var creds = CourierCredentials.from(existingCourier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);

        Courier duplicateCourier = new Courier(existingCourier.getLogin(), existingCourier.getPassword(), existingCourier.getFirstName());
        ValidatableResponse duplicateCreateResponse = client.createCourier((duplicateCourier));

        duplicateCreateResponse.statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .body("message", is(DOUBLE_COURIER_MESSAGE));

    }
    @Test
    @DisplayName("Создание курьера с не заполненным login")
    public void courierCreateWhithoutLogin(){
        var courier = Courier.random();
        courier.setLogin(null);
        ValidatableResponse response = client.createCourier(courier);
        response.statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", containsString(NEGATIVE_AUTH_MESSAGE));


    }
    @Test
    @DisplayName("Создание курьера с не заполненным password")
    public void courierCreateWhithoutPassword(){
        var courier = Courier.random();
        courier.setPassword(null);
        ValidatableResponse response = client.createCourier(courier);
        response.statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", containsString(NEGATIVE_AUTH_MESSAGE));

    }
    @Test
    @DisplayName("Создание курьера с не заполненным firstName")
    public void courierCreateWhithoutFirstName(){
        var courier = Courier.random();
        courier.setFirstName(null);
        ValidatableResponse response = client.createCourier(courier);
        response.statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", containsString(NEGATIVE_AUTH_MESSAGE));

    }

}
