package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.AuthClient;
import wbd.api.dto.AuthRequestDto;
import wbd.core.TestBaseRA;


public class RegisterTests extends TestBaseRA {

    SoftAssert softAssert = new SoftAssert();
    @Test
    public void testRegisterNewUserPositive() {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@mail.com";

        AuthRequestDto request = AuthRequestDto.builder()
                .email(uniqueEmail)
                .password("11111111")
                .build();

        Response response = AuthClient.register(request);
        logger.info("Регистрация пользователя: {}", response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Ожидается статус 200 OK при регистрации");
        softAssert.assertAll();
    }

}
