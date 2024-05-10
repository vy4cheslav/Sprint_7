package orders;

import config.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static config.Constants.ORDER_CANCEL_PATH;
import static config.Constants.ORDER_PATH;

public class OrderClient extends Client {

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return spec()
                .when()
                .get(ORDER_PATH)
                .then().log().all()
                ;
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track) {
        return spec()
                .body(track)
                .when()
                .put(ORDER_CANCEL_PATH)
                .then();
    }
}