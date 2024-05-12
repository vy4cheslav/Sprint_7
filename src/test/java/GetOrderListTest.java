import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import orders.OrderClient;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient  = new OrderClient();
    }

    @Test
    @Description("Получение списка зазказов")
    public void getOrderList(){
        ValidatableResponse response = orderClient.getOrderList();
        response.assertThat().statusCode(HttpURLConnection.HTTP_OK).body("orders.id", notNullValue());

    }

}
