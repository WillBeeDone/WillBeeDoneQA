package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import wbd.api.client.AuthClient;
import wbd.api.client.get_post.ApiClient_PostAddOffer;
import wbd.api.dto.AddOfferRequestDto;
import wbd.api.dto.AddOfferResponseDto;
import wbd.api.dto.AuthRequestDto;
import wbd.api.dto.LoginResponseDto;
import wbd.core.TestBaseRA;
import wbd.utils.RetryAnalyzer;

import java.util.List;

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

    @Test
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
        softAssert.assertNotNull(responseDto, "Ответ не должен быть null");

        if (responseDto != null) {
            softAssert.assertTrue(responseDto.getId() > 0, "id оффера должен быть больше 0");
            softAssert.assertEquals(responseDto.getTitle(), requestDto.getTitle(), "title должен совпадать");
            softAssert.assertEquals(responseDto.getPricePerHour(), requestDto.getPricePerHour(), "цена должна совпадать");
            softAssert.assertEquals(responseDto.getDescription(), requestDto.getDescription(), "описание должно совпадать");
            softAssert.assertNotNull(responseDto.getCategory(), "категория не должна быть null");

            if (responseDto.getCategory() != null) {
                softAssert.assertEquals(responseDto.getCategory().getName(), requestDto.getCategoryName(), "название категории должно совпадать");
            }

            softAssert.assertNotNull(responseDto.getImages(), "список изображений не должен быть null");
            logger.info("✅ оффер успешно добавлен с id: {}", responseDto.getId());
        }

        logger.info("---------------------------------------------------------");
        softAssert.assertAll();
    }

    @Test(groups = "Negative")
    public void testAddOfferWithoutAuth() {
        logger.info("начинается тест: добавление оффера без авторизации");
        logger.info("=============================================");

        // создаем DTO запроса без токена
        AddOfferRequestDto requestDto = AddOfferRequestDto.builder()
                .pricePerHour(25.0)
                .description("Description ta-ta-ta-ta...")
                .categoryName("Plumbing")
                .title("Test Title")
                .images(List.of("img.jpg"))
                .build();

        // отправляем запрос без токена
        Response response = ApiClient_PostAddOffer.addOffer(requestDto, null);
        logger.info("ответ на добавление оффера без авторизации: {}", response.asString());

        // проверка статус-кода (ожидаем ошибку 401)
        softAssert.assertEquals(response.getStatusCode(), 401, "Expected status 401 Unauthorized");

        softAssert.assertAll();
    }
}
