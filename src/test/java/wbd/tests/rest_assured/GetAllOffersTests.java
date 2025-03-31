package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.core.TestBaseRA;
import wbd.api.сlient.dto.AllOffersResponseDto;
import wbd.api.сlient.get.ApiClient_GetAllOffers;


import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllOffersTests extends TestBaseRA {

    @Test
    public void testGetAllOffers() {

        logger.info("Start testing GetAllOffers");
        logger.info("=============================================");

        // получаем все офферы
        Response response = ApiClient_GetAllOffers.getAllOffers();
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // статус 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("The test was successful: Response code 200 (OK)");
        } else {
            logger.error("Error: Received response with code " + response.getStatusCode());
        }

        // парсим JSON в список объектов AllOffersResponseDto
        List<AllOffersResponseDto> offers = response.jsonPath().getList("content", AllOffersResponseDto.class);

        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertTrue(!offers.isEmpty(), "The Offers list must not be empty");

        // проверяем данные первого оффера, если список не пуст
        if (!offers.isEmpty()) {
            AllOffersResponseDto offer = offers.get(0);

            softAssert.assertNotNull(offer.getTitle(), "Title must not be null");
            softAssert.assertTrue(!offer.getTitle().isEmpty(), "Title should not be empty");

            softAssert.assertNotNull(offer.getDescription(), "Description must not be null");
            softAssert.assertTrue(!offer.getDescription().isEmpty(), "Description should not be empty");

            softAssert.assertTrue(offer.getPricePerHour() > 0, "The price has to be positive");

            softAssert.assertNotNull(offer.getCategoryDto(), "Category must not be null");
            softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category Name must not be null");

            softAssert.assertNotNull(offer.getUserFilterResponseDto(), "User must not be null");

            if (offer.getUserFilterResponseDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName must not be null");
                softAssert.assertTrue(!offer.getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName must not be empty");

                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "LocationResponseDto must not be null");

                if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                    softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "CityName must not be null");
                    softAssert.assertTrue(offer.getUserFilterResponseDto().getLocationDto().getCityName().length() > 0, "CityName must not be empty");
                }
            }

            logger.info("---------------------------------------------------------");
            for (AllOffersResponseDto offerDto : offers ) {
                logger.error("Offer => " + offerDto.getTitle() + ";");
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
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        //  статус 404
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid endpoint");

        logger.error("Error: Received response with code " + response.getStatusCode());

        softAssert.assertAll();
    }

    @Test   // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_400() {
        Response response = given()
                .queryParam("pricePerHour", "invalid-price") // некорректный параметр
                .when()
                .get("/offers")
                .then()
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // статус 400
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid query param");

        logger.error("Error: Received response with code " + response.getStatusCode());

        softAssert.assertAll();
    }

    @Test  // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_Category_400() {
        Response response = given()
                .queryParam("category", "nonexistent-category") // некорректное значение категории
                .when()
                .get("/offers")
                .then()
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "category"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'category' query param");

        logger.error("Error: Received response with code " + response.getStatusCode() + " when transmitting an invalid category");

        softAssert.assertAll();
    }

    @Test  // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_Location_400() {
        Response response = given()
                .queryParam("cityName", 123) // Некорректное значение для города
                .when()
                .get("/offers")
                .then()
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "cityName"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'cityName' query param");

        logger.error("Error: Received response with code " + response.getStatusCode() + " when transmitting an incorrect city name");

        softAssert.assertAll();
    }
}