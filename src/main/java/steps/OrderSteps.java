package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.Assert;

import static constants.URLs.ORDERS_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class OrderSteps extends Client{
    @Step("Создание заказа без access token")
    public ValidatableResponse createOrderWithoutToken(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then();
    }

    @Step("Создание заказа с access token")
    public ValidatableResponse createOrderWithToken(String accessToken, Order order) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDERS_URL)
                .then();
    }

    @Step("Список заказов без access token")
    public ValidatableResponse listOfOrdersWithoutToken() {
        return given()
                .spec(getSpec())
                .body("")
                .when()
                .get(ORDERS_URL)
                .then();
    }

    @Step("Список заказов с access token")
    public ValidatableResponse listOfOrdersWithToken(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body("")
                .when()
                .get(ORDERS_URL)
                .then();
    }

    @Step("Проверка ответа при создании заказа без ингредиентов")
    public void checkAnswerWithoutIngredients(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(false))
                .statusCode(400);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Ingredient ids must be provided", actualMessage);
    }

    @Step("Проверка ответа при создании заказа с неправильным хэшем ингредиента")
    public void checkAnswerWithWrongHash(ValidatableResponse validatableResponse) {
        validatableResponse
                .statusCode(500);
    }

    @Step("Проверка ответа при получении списка заказов от неавторизованного пользователя")
    public void checkAnswerGetListNonAuth(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }
}
