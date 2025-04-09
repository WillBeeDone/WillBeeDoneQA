package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wbd.api.client.AuthClient;
import wbd.api.dto.AuthRequestDto;
import wbd.api.dto.LoginResponseDto;
import wbd.core.TestBaseRA;
import wbd.utils.RetryAnalyzer;

public class LoginTests extends TestBaseRA {
    private final String validEmail = "wbdqatest52@gmail.com"; // подтверждённый пользователь
    private final String validPassword = "Password!123";
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

    @Test()
    public void loginSimpleTestWithoutAssert() {
        Response response = AuthClient.login(body);
        response
                .then()
                .log()
                .all();
    }


    @Test(retryAnalyzer = RetryAnalyzer.class, groups = "Positive")
    public void loginSuccessTestPositive() {
        Response response = AuthClient.login(body);
        logger.info("Login response: {}", response.asString());

        int statusCode = response.getStatusCode();
        softAssert.assertEquals(statusCode, 200, "Expected 200 OK");

        if (statusCode == 200) {
            LoginResponseDto user = response.as(LoginResponseDto.class);

            softAssert.assertNotNull(user, "TokenResponseDto should not be null");

            if (user != null) {
                softAssert.assertNotNull(user.getAccessToken(), "accessToken should not be null");
                softAssert.assertNotNull(user.getRefreshToken(), "refreshToken should not be null");

                logger.info("Access Token: {}", user.getAccessToken());
                logger.info("Refresh Token: {}", user.getRefreshToken());
            }
        } else {
            logger.error("Login failed: status code = {}, body = {}", statusCode, response.asPrettyString());
            softAssert.fail("Login failed: unexpected status code " + statusCode);
        }

        softAssert.assertAll();
    }


    @Test(groups = "Negative")
    public void loginWrongPasswordTestNegative() {
        Response loginResponse = AuthClient.login(errorBody);
        logger.info("Wrong password login response: {}", loginResponse.asString());

        softAssert.assertEquals(loginResponse.getStatusCode(), 401, "Status is expected 401 Unauthorized");

        softAssert.assertAll();
    }
}