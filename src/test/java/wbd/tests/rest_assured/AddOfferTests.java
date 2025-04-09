package wbd.tests.rest_assured;

import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import wbd.api.client.AuthClient;
import wbd.api.client.get_post.ApiClient_PostAddOffer;
import wbd.api.dto.AddOfferRequestDto;
import wbd.api.dto.AddOfferResponseDto;
import wbd.api.dto.AuthRequestDto;
import wbd.api.dto.LoginResponseDto;
import wbd.core.TestBaseRA;

import java.util.List;

@Epic("Offers")
@Feature("Add new offer")
public class AddOfferTests extends TestBaseRA {

    private String accessToken;

    @BeforeMethod
    public void loginAndGetToken() {
        logger.info("A login is performed for obtaining Access token...");

        // Подготовка данных для логина
        AuthRequestDto loginBody = AuthRequestDto.builder()
                .email("wbdqatest52@gmail.com")
                .password("Password!123")
                .build();

        // Выполнение запроса на логин
        Response loginResponse = AuthClient.login(loginBody);

        // Проверка успешности логина
        logger.info("login response: {}", loginResponse.asString());

        int loginStatusCode = loginResponse.getStatusCode();
        softAssert.assertEquals(loginStatusCode, 200, "Expected 200 OK");

        if (loginStatusCode == 200) {
            // Извлечение токена из ответа
            LoginResponseDto user = loginResponse.as(LoginResponseDto.class);
            softAssert.assertNotNull(user, "TokenResponseDto should not be null");

            if (user != null) {
                accessToken = user.getAccessToken();  // Сохраняем accessToken
                logger.info("Access Token: {}", accessToken);
            }
        } else {
            logger.error("Login failed: status code = {}, body = {}", loginStatusCode, loginResponse.asPrettyString());
            softAssert.fail("Login failed: unexpected status code " + loginStatusCode);
        }

        softAssert.assertAll();
    }

    @Test(groups = "Positive")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Authorized user can create a new offer")
    @Description("Verify that a logged-in user can successfully add a new offer with valid data")
    @TmsLink("")
    public void testAddOffer() {

        logger.info("The test begins: adding a new offer");
        logger.info("=============================================");

        // создаем DTO запроса
        AddOfferRequestDto requestDto = AddOfferRequestDto.builder()
                .pricePerHour(25.0)
                .description("Cleaning the pipes of butterflies")
                .categoryName("Plumber")
                .title("Butterfly exterminator")
                .images(List.of("img.jpg"))
                .build();

        // отправляем запрос
        Response response = ApiClient_PostAddOffer.addOffer(requestDto, accessToken);
        logger.info("ответ на добавление оффера: {}", response.asString());

        // проверка статус-кода
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status 200 OK");

        // парсим ответ
        AddOfferResponseDto responseDto = response.as(AddOfferResponseDto.class);
        softAssert.assertNotNull(responseDto, "The answer should not be null ");

        if (responseDto != null) {
            softAssert.assertTrue(responseDto.getId() > 0, "Offer ID should be more than 0");
            softAssert.assertEquals(responseDto.getTitle(), requestDto.getTitle(), "Title must match");
            softAssert.assertEquals(responseDto.getPricePerHour(), requestDto.getPricePerHour(), "The price should match");
            softAssert.assertEquals(responseDto.getDescription(), requestDto.getDescription(), "The description must match");
            softAssert.assertNotNull(responseDto.getCategory(), "The category should not be null");

            if (responseDto.getCategory() != null) {
                softAssert.assertEquals(responseDto.getCategory().getName(), requestDto.getCategoryName(), "The name of the category must coincide");
            }

            softAssert.assertNotNull(responseDto.getImages(), "The list of images should not be null");
            logger.info("✅ Offer Successfully Added with ID: {}", responseDto.getId());
        }

        logger.info("---------------------------------------------------------");
        softAssert.assertAll();
    }

    @Test(groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unauthorized user tries to create an offer")
    @Description("Verify that user cannot add an offer without being authenticated")
    @TmsLink("")
    public void testAddOfferWithoutAuth() {
        logger.info("The test begins: adding an offer without authorization ");
        logger.info("=============================================");

        // создаем DTO запроса без токена
        AddOfferRequestDto requestDto = AddOfferRequestDto.builder()
                .pricePerHour(25.0)
                .description("Description uuuuuuuuuuuuuuuuupssssss...")
                .categoryName("Plumbing")
                .title("i'm a virus")
                .images(List.of("img.jpg"))
                .build();

        // отправляем запрос без токена
        Response response = ApiClient_PostAddOffer.addOffer(requestDto, null);
        logger.info("The answer to the addition of an offer without authorization: {}", response.asString());

        // проверка статус-кода (ожидаем ошибку 403)
        softAssert.assertEquals(response.getStatusCode(), 403, "Expected status 401 Unauthorized");

        softAssert.assertAll();
    }
}
