import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static constants.RandomData.*;

public class TestUser {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Успешное создание пользователя с email, паролем и именем")
    public void createUniqueUserSuccess() {
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, randomPassword, randomName);
        userSteps.checkAnswerSuccess(responseCreate);
        accessToken = userSteps.getAccessToken(responseCreate);
    }

    @Test
    @DisplayName("Создание существующего пользователя")
    public void createExistingUser() {
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, randomPassword, randomName);
        accessToken = userSteps.getAccessToken(responseCreate);
        ValidatableResponse responseIdentical = userSteps.createUser(randomEmail, randomPassword, randomName);
        userSteps.checkAnswerAlreadyExist(responseIdentical);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser("", randomPassword, randomName);
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без password")
    public void createUserWithoutPasswordForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, "", randomName);
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @Test
    @DisplayName("Создание пользователя без name")
    public void createUserWithoutNameForbidden() {
        ValidatableResponse responseCreate = userSteps.createUser(randomEmail, randomPassword, "");
        userSteps.checkAnswerForbidden(responseCreate);
    }

    @After
    public void close() {
        userSteps.deleteUsersAfterTests(accessToken);
    }

}
