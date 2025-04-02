package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.AuthClient;
import wbd.api.dto.AuthRequestDto;
import wbd.api.dto.TokenResponseDto;
import wbd.core.TestBaseRA;
import wbd.utils.RetryAnalyzer;

public class LoginTests extends TestBaseRA {
    SoftAssert softAssert = new SoftAssert();

    private final String validEmail = "john@gmail.com"; // подтверждённый пользователь
    private final String validPassword = "11111111";
    private final String invalidPassword = "111111112";
    private AuthRequestDto body;
    private AuthRequestDto errorBody;

    @BeforeClass
    public void setupLoginBody() {

        body = AuthRequestDto.builder()
                .email(validEmail)
                .password(validPassword)
                .build();

        errorBody = AuthRequestDto.builder()
                .email(validEmail)
                .password(invalidPassword)
                .build();

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void loginSimpleTestWithoutAssert() {
        Response response = AuthClient.login(body);
        response
                .then()
                .log()
                .all();
    }


    //  баг-репорт
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void loginSuccessTest() {
        Response response = AuthClient.login(body);
        logger.info("Login response: {}", response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

        TokenResponseDto tokens = response.as(TokenResponseDto.class);
        softAssert.assertNotNull(tokens, "TokenResponseDto should not be null");

        if (tokens != null) {
            softAssert.assertNotNull(tokens.getAccessToken(), "accessToken should not be null");
            softAssert.assertNotNull(tokens.getRefreshToken(), "refreshToken should not be null");

            logger.info("Access Token: {}", tokens.getAccessToken());
            logger.info("Refresh Token: {}", tokens.getRefreshToken());
        }

        softAssert.assertAll();
    }

    // баг-репорт
    @Test
    public void loginWrongPasswordTest() {
        Response loginResponse = AuthClient.login(errorBody);
        logger.info("Wrong password login response: {}", loginResponse.asString());

        softAssert.assertEquals(loginResponse.getStatusCode(), 401, "Ожидается статус 401 Unauthorized");

        softAssert.assertAll();
    }
}