import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static constants.RandomData.*;

public class TestOrderList {
    private UserSteps userSteps;
    private Order order;
    private OrderSteps orderSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userSteps.createUser(randomEmail, randomPassword, randomName);
        ValidatableResponse responseLogin = userSteps.login(randomEmail, randomPassword);
        accessToken = userSteps.getAccessToken(responseLogin);
        order = new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"));
        orderSteps.createOrderWithToken(accessToken, order);
    }

    @Test
    @DisplayName("Получение списка конкретного пользователя c access token")
    public void getListOfOrdersAuthSuccess() {
        ValidatableResponse responseGetList = orderSteps.listOfOrdersWithToken(accessToken);
        userSteps.checkAnswerSuccess(responseGetList);
    }

    @Test
    @DisplayName("Получение списка конкретного пользователя без access token")
    public void getListOfOrdersNonAuthUnauthorized() {
        ValidatableResponse responseGetList = orderSteps.listOfOrdersWithoutToken();
        orderSteps.checkAnswerGetListNonAuth(responseGetList);
    }

    @After
    public void close() {
        userSteps.deleteUsersAfterTests(accessToken);
    }

}
