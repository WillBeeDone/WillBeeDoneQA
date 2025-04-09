package wbd.tests.rest_assured;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.AuthClient;
import wbd.api.dto.AuthRequestDto;
import wbd.core.TestBaseRA;
import wbd.utils.DataProviders;


@Epic("Authorization")
@Feature("Registration API")
@Listeners({AllureTestNg.class})
public class RegisterTests extends TestBaseRA {
    String uniqueEmail = "user" + System.currentTimeMillis() + "@mail.com";

    /**
     * Тест: Валидный пароль (для контроля)
     */
    @Test(groups = "Positive")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Successful registration")
    @Description("Registers a user with a valid complex password")
    @TmsLink("")
    public void testRegisterWithComplexPassword_SuccessPositive() {

        AuthRequestDto request = AuthRequestDto.builder()
                .email(uniqueEmail)
                .password("Test@123A")
                .build();

        Response response = AuthClient.register(request);
        logger.info("Registration with valid password: {}", response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK");
        softAssert.assertAll();
    }

    @Test(dataProvider = "validPasswords", dataProviderClass = DataProviders.class, groups = "Positive")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Password validation")
    @Description("Tests multiple valid passwords with unique email")
    @TmsLink("")
    public void testValidPasswordsWithUniqueEmailPositive(String password, String description) {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@mail.com";

        AuthRequestDto request = AuthRequestDto.builder()
                .email(uniqueEmail)
                .password(password)
                .build();

        Response response = AuthClient.register(request);
        logger.info("{} | Email: {} | Status: {}", description, uniqueEmail, response.getStatusCode());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK for valid password");
        softAssert.assertAll();
    }


    /**
     * Тест: Пароль из Swagger
     */
    @Test(groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Registration with weak password")
    @Description("Attempts to register a user with an invalid password from Swagger example")
    @TmsLink("")
    public void testRegisterNewUserNegative() {

        AuthRequestDto request = AuthRequestDto.builder()
                .email(uniqueEmail)
                .password("11111111")
                .build();

        Response response = AuthClient.register(request);
        logger.info("Registration with weak/simple password: {}", response.asString());

        softAssert.assertEquals(response.getStatusCode(), 400, "Expected 400 Bad Request for weak password");
        softAssert.assertAll();
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class, groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid password rejection")
    @Description("Checks that server rejects invalid passwords for registration")
    @TmsLink("")
    public void testInvalidPasswordsWithUniqueEmailNegative(String password, String description) {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@mail.com";

        if (password.equals("user@mail.com")) {
            password = uniqueEmail;
        }

        AuthRequestDto request = AuthRequestDto.builder()
                .email(uniqueEmail)
                .password(password)
                .build();

        Response response = AuthClient.register(request);
        logger.warn("[BUG] {} | Email: {} | Password: '{}' | Status: {}",
                description, uniqueEmail, password, response.getStatusCode());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotEquals(
                response.getStatusCode(),
                200,
                "BUG: '" + description + "' — server should NOT accept this password"
        );

        softAssert.assertAll();
    }
}
