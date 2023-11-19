import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static constants.RandomData.*;

public class TestCreateOrder {
    private UserSteps userSteps;
    private Order order;
    private OrderSteps orderSteps;
    private String accessToken;
    private List<String> ingredients;
    private List<String> wrongIngredients;
    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userSteps.createUser(randomEmail, randomPassword, randomName);
        ValidatableResponse responseLogin = userSteps.login(randomEmail, randomPassword);
        accessToken = userSteps.getAccessToken(responseLogin);
        ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        wrongIngredients = new ArrayList<>();
        wrongIngredients.add("123zxc");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthorizationSuccess() {
        order = new Order(ingredients);
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithToken(accessToken, order);
        userSteps.checkAnswerSuccess(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    public void createOrderAuthWithoutIngredientsBadRequest() {
        order = new Order();
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithToken(accessToken, order);
        orderSteps.checkAnswerWithoutIngredients(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с неправильным хэшем ингредиента")
    public void createOrderAuthWithWrongHashInternalServerError() {
        order = new Order(wrongIngredients);
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithToken(accessToken, order);
        orderSteps.checkAnswerWithWrongHash(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthorizationSuccess() {
        order = new Order(ingredients);
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithoutToken(order);
        userSteps.checkAnswerSuccess(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа без авторизации без ингредиентов")
    public void createOrderNonAuthWithoutIngredientsBadRequest() {
        order = new Order();
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithoutToken(order);
        orderSteps.checkAnswerWithoutIngredients(responseCreateAuth);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с неправильным хэшем ингредиента")
    public void createOrderNonAuthWithWrongHashInternalServerError() {
        order = new Order(wrongIngredients);
        ValidatableResponse responseCreateAuth = orderSteps.createOrderWithoutToken(order);
        orderSteps.checkAnswerWithWrongHash(responseCreateAuth);
    }

    @After
    public void close() {
        userSteps.deleteUsersAfterTests(accessToken);
    }

}
