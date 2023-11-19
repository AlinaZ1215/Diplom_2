package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Login;
import model.User;
import org.junit.Assert;

import static constants.URLs.*;
import static constants.URLs.AUTH_USER_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class UserSteps extends Client{
    @Step("Создание пользователя")
    public ValidatableResponse createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(AUTH_REGISTER_URL)
                .then();

    }



    @Step("Авторизация с access token")
    public ValidatableResponse authorizationWithToken(String accessToken, String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(AUTH_USER_URL)
                .then();
    }

    @Step("Авторизация без access token")
    public ValidatableResponse authorizationWithoutToken(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(AUTH_USER_URL)
                .then();
    }

    @Step("Вход пользователя")
    public ValidatableResponse login(String email, String password) {
        Login login = new Login(email, password);
        return given()
                .spec(getSpec())
                .body(login)
                .when()
                .post(AUTH_LOGIN_URL)
                .then();
    }

    @Step("Получение access token")
    public String getAccessToken(ValidatableResponse validatableResponse) {
        return validatableResponse.extract().path("accessToken");
    }

    @Step("Проверяем тело и статус ответа при создании, изменении пользователя или получении списка заказов - 200")
    public void checkAnswerSuccess(ValidatableResponse validatableResponse) {
        validatableResponse
                .body("success", is(true))
                .statusCode(200);
    }

    @Step("Проверяем ответ после создания уже зарегистрированного пользователя - 403")
    public void checkAnswerAlreadyExist(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("User already exists", actualMessage);
    }

    @Step("Проверяем ответ при создании пользователя без обязательного поля: электронной почты, пароля и имени - 403")
    public void checkAnswerForbidden(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(403);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("Email, password and name are required fields", actualMessage);
    }

    @Step("Проверяем ответ после входа в систему с неправильными учетными данными - 401")
    public void checkAnswerWithWrongData(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("email or password are incorrect", actualMessage);
    }

    @Step("Проверяем ответ после изменения данных пользователя без access token - 401")
    public void checkAnswerWithoutToken(ValidatableResponse validatableResponse) {
        validatableResponse.assertThat()
                .body("success", is(false))
                .and().statusCode(401);
        String actualMessage = validatableResponse.extract().path("message").toString();
        Assert.assertEquals("You should be authorised", actualMessage);
    }
    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(AUTH_USER_URL);
    }
    @Step("Удаление пользователя после тестов")
    public void deleteUsersAfterTests(String accessToken) {
        if (accessToken != null) {
            deleteUser(accessToken);
        } else {
            given().spec(getSpec())
                    .when()
                    .delete(AUTH_USER_URL);
        }
    }
}
