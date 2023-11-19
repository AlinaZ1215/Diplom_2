import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.RandomData.*;
import static constants.RandomData.randomName;

public class TestChangingUserData {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void createChangingDataWithAuth() {
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, randomPassword, randomName);
        userSteps.checkAnswerSuccess(responseCreate);
        ValidatableResponse responseLogin = userSteps.login(randomEmail, randomPassword);
        userSteps.checkAnswerSuccess(responseLogin);
        accessToken = userSteps.getAccessToken(responseLogin);
        ValidatableResponse responseChangeWithToken = userSteps.authorizationWithToken(accessToken, "x" + randomEmail, "x" + randomPassword, "x" + randomName);
        userSteps.checkAnswerSuccess(responseChangeWithToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void createChangingDataWithoutAuth() {
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, randomPassword, randomName);
        userSteps.checkAnswerSuccess(responseCreate);
        ValidatableResponse responseLogin = userSteps.login(randomEmail, randomPassword);
        userSteps.checkAnswerSuccess(responseLogin);
        accessToken = userSteps.getAccessToken(responseLogin);
        ValidatableResponse responseChangeWithoutToken = userSteps.authorizationWithoutToken("x" + randomEmail, "x" + randomPassword, "x" + randomName);
        userSteps.checkAnswerWithoutToken(responseChangeWithoutToken);
    }

    @After
    public void close() {
        userSteps.deleteUsersAfterTests(accessToken);
    }

}
