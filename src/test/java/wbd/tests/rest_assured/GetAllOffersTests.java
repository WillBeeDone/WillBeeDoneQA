package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.OffersClient;
import wbd.core.TestBaseRA;
import wbd.dto.AllOffersResponseDto;

import java.util.List;
import static io.restassured.RestAssured.given;

public class GetAllOffersTests extends TestBaseRA {
    private static final Logger logger = LoggerFactory.getLogger(GetAllOffersTests.class);
    @Test
    public void testGetAllOffers() {
        // получаем все офферы
        Response response = OffersClient.getAllOffers();
        logger.info("Response body: {}", response.asString());

        SoftAssert softAssert = new SoftAssert();

        // статус 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("Тест прошел успешно: Код ответа 200 (OK)");
        } else {
            logger.error("Ошибка: Получен ответ с кодом {}", response.getStatusCode());
        }

        // парсим JSON в список объектов AllOffersResponseDto только если статус 200

        List<AllOffersResponseDto> offers = response.jsonPath().getList("", AllOffersResponseDto.class);
        logger.info("Получено офферов: {}", offers.size());

        softAssert.assertNotNull(offers, "Offers list не должен быть null");
        softAssert.assertTrue(!offers.isEmpty(), "Offers list не должен быть пустым");

        // проверяем данные первого оффера, если список не пуст
        if (!offers.isEmpty()) {
            AllOffersResponseDto offer = offers.get(0);

            softAssert.assertNotNull(offer.getTitle(), "Title не должен быть null");
            softAssert.assertTrue(offer.getTitle().length() > 0, "Title не должен быть пустым");

            softAssert.assertNotNull(offer.getDescription(), "Description не должен быть null");
            softAssert.assertTrue(offer.getDescription().length() > 0, "Description не должен быть пустым");

            softAssert.assertTrue(offer.getPricePerHour() > 0, "Цена должна быть положительной");

            softAssert.assertNotNull(offer.getCategoryResponseDto(), "Category не должен быть null");
            softAssert.assertNotNull(offer.getCategoryResponseDto().getName(), "Name категории не должен быть null");

            softAssert.assertNotNull(offer.getUserFilterResponseDto(), "User не должен быть null");

            if (offer.getUserFilterResponseDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName не должен быть null");
                softAssert.assertTrue(offer.getUserFilterResponseDto().getFirstName().length() > 0, "FirstName не должен быть пустым");

                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationResponseDto(), "LocationResponseDto не должен быть null");

                if (offer.getUserFilterResponseDto().getLocationResponseDto() != null) {
                    softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationResponseDto().getCityName(), "CityName не должен быть null");
                    softAssert.assertTrue(offer.getUserFilterResponseDto().getLocationResponseDto().getCityName().length() > 0, "CityName не должен быть пустым");
                }
            }

            logger.info("---------------------------------------------------------");
            for (AllOffersResponseDto offerDto : offers ) {
                logger.info("Offer => {}", offerDto.getTitle());
            }
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetAllOffersWithInvalidEndpoint_404() {
        Response response = given()
                .when()
                .get("/invalid-offers") // неправильный эндпоинт
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        //  статус 404
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid endpoint");

        logger.warn("Получен ответ с кодом {} для несуществующего эндпоинта", response.getStatusCode());

        softAssert.assertAll();
    }

    @Test   // не возвращает 400 - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_400() {
        Response response = given()
                .queryParam("pricePerHour", "invalid-price") // некорректный параметр
                .when()
                .get("/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // статус 400
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid query param");

        logger.warn("Получен код ответа {} при передаче некорректного параметра pricePerHour", response.getStatusCode());

        softAssert.assertAll();
    }

    @Test  // не возвращает 400 - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_Category_400() {
        Response response = given()
                .queryParam("category", "nonexistent-category") // некорректное значение категории
                .when()
                .get("/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "category"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'category' query param");

        logger.warn("Получен код ответа {} при передаче несуществующей категории", response.getStatusCode());

        softAssert.assertAll();
    }

    @Test  // не возвращает 400 - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_UserFirstName_400() {
        Response response = given()
                .queryParam("firstName", "") // Пустое значение для имени пользователя
                .when()
                .get("/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "firstName"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'firstName' query param");

        logger.warn("Получен код ответа {} при передаче пустого firstName", response.getStatusCode());

        softAssert.assertAll();
    }

    @Test  // не возвращает 400 - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_Location_400() {
        Response response = given()
                .queryParam("cityName", 123) // Некорректное значение для города
                .when()
                .get("/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "cityName"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'cityName' query param");

        logger.warn("Получен код ответа {} при передаче некорректного cityName", response.getStatusCode());
        softAssert.assertAll();
    }
}

