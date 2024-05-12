import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import orders.Order;
import orders.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class OrderParamTest {
    private final List<String> colour;
    private int track;
    private OrderClient orderClient;

    public OrderParamTest(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters
    public static Object[][] getScooterColour() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK, GRAY")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    @Step("Отменить тестовый заказ")
    public void cancelOrder() {
        orderClient.cancelOrder(track);
    }

    @Test
    @DisplayName("Размещение заказа с самокатами разных цветов")
    public void orderingScootersInDifferentColors() {
        Order order = new Order(colour);
        ValidatableResponse response = orderClient.createOrder(order);
        track = response.extract().path("track");
        response.assertThat().statusCode(HttpURLConnection.HTTP_CREATED).body("track", is(notNullValue()));

    }
}
