import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.RandomData.*;

public class TestLoginUser {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, randomPassword, randomName);
        accessToken = userSteps.getAccessToken(responseCreate);
    }

    @Test
    @DisplayName("Успешный вход пользователя в систему")
    public void createLoginUserSuccess() {
        ValidatableResponse responseLogin = userSteps.login(randomEmail, randomPassword);
        userSteps.checkAnswerSuccess(responseLogin);
    }

    @Test
    @DisplayName("Вход пользователя с неправильным email")
    public void createLoginUserWithWrongEmailUnauthorized() {
        ValidatableResponse responseLogin = userSteps.login("wrongEmail@yandex.ru", randomPassword);
        userSteps.checkAnswerWithWrongData(responseLogin);
    }

    @Test
    @DisplayName("Вход пользователя с неправильным password")
    public void createLoginUserWithWrongPassUnauthorized() {
        ValidatableResponse responseLogin = userSteps.login(randomEmail, "123456");
        userSteps.checkAnswerWithWrongData(responseLogin);
    }

    @After
    public void close() {
        userSteps.deleteUsersAfterTests(accessToken);
    }

}
