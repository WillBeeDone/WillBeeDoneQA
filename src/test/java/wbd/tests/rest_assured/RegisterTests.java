package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.AuthClient;
import wbd.api.dto.AuthRequestDto;
import wbd.core.TestBaseRA;
import wbd.utils.DataProviders;


public class RegisterTests extends TestBaseRA {

    SoftAssert softAssert = new SoftAssert();
    String uniqueEmail = "user" + System.currentTimeMillis() + "@mail.com";


    /**
     * Тест: Валидный пароль (для контроля)
     */
    @Test
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

    @Test(dataProvider = "validPasswords", dataProviderClass = DataProviders.class)
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
    @Test
    public void testRegisterNewUserNegative() {

        AuthRequestDto request = AuthRequestDto.builder()
                .email(uniqueEmail)
                .password("11111111")
                .build();

        Response response = AuthClient.register(request);
        logger.info("Registration with weak/simple password: {}", response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200 OK during registration");
        softAssert.assertAll();
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class)
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
