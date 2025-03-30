package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.core.TestBaseRA;
import wbd.api.сlient.dto.FilteredOffersResponseDto;
import wbd.api.сlient.get.ApiClient_GetOfferById;


import static io.restassured.RestAssured.given;

public class GetOfferByIdTests extends TestBaseRA {

    @Test
    public void testGetOfferById() {

        logger.info("Start testing GetOfferById");
        logger.info("=============================================");

        int offerId = 1;  // допустим, оффер с ID 1 существует

        Response response = ApiClient_GetOfferById.getOfferById(offerId); // получаем оффер по ID
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // проверка статуса 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("The test was successful: Response code 200 (OK)");
        } else {
            logger.error("Error: Received response with code " + response.getStatusCode());
        }

        // парсим JSON в объект FilteredOffersResponseDto
        FilteredOffersResponseDto offer = response.as(FilteredOffersResponseDto.class);

        softAssert.assertNotNull(offer, "Offer must not be null");

        // все необходимые поля присутствуют
        softAssert.assertNotNull(offer.getTitle(), "Title must not be null");
        softAssert.assertTrue(!offer.getTitle().isEmpty(), "Title should not be empty");

        softAssert.assertNotNull(offer.getDescription(), "Description must not be null");
        softAssert.assertTrue(!offer.getDescription().isEmpty(), "Description should not be empty");

        softAssert.assertTrue(offer.getPricePerHour() > 0, "The price has to be positive");

        // проверка categoryResponseDto как объекта
        softAssert.assertNotNull(offer.getCategoryDto(), "Category must not be null");
        softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category Name must not be null");
        softAssert.assertTrue(!offer.getCategoryDto().getName().isEmpty(), "Category name must not be empty");

        // проверка UserFilterResponseDto (если оно есть)
        if (offer.getUserFilterResponseDto() != null) {
            softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName must not be null");
            softAssert.assertTrue(!offer.getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName must not be empty");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLastName(), "LastName must not be null");
            softAssert.assertTrue(!offer.getUserFilterResponseDto().getLastName().isEmpty(), "LastName must not be empty");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getProfilePicture(), "ProfilePicture must not be null");
            softAssert.assertTrue(!offer.getUserFilterResponseDto().getProfilePicture().isEmpty(), "ProfilePicture must not be empty");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "LocationResponseDto must not be null");

            if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "CityName must not be null");
                softAssert.assertTrue(!offer.getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "CityName must not be empty");
            }
        }

        // проверка на наличие хотя бы одного изображения
        if (offer.getImages() != null && !offer.getImages().isEmpty()) {
            softAssert.assertNotNull(offer.getImages().get(0).getImageUrl(), "ImageUrl must not be null");
            softAssert.assertTrue(!offer.getImages().get(0).getImageUrl().isEmpty(), "ImageUrl must not be empty");
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetOfferById_InvalidId() {
        int invalidId = -1; // некорректный ID
        Response response = ApiClient_GetOfferById.getOfferById(invalidId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotEquals(response.getStatusCode(), 200, "An error is expected for an invalid ID");

        softAssert.assertAll();
    }

    @Test
    public void testGetOfferByNonExistentId_404() {
        int nonExistentOfferId = 9999;  // предположим, что такого оффера нет
        Response response = ApiClient_GetOfferById.getOfferById(nonExistentOfferId); // получаем оффер по несуществующему ID

        SoftAssert softAssert = new SoftAssert();

        // проверка, что сервер вернул 404 (Not Found)
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for non-existent offer");

        if (response.getStatusCode() == 404) {
            logger.error("Error: Received response with code 404 (Not Found)");
        }

        softAssert.assertAll();
    }

    @Test  // написать баг-репорт
    public void testGetOffersByInvalidId_400() {
        // запрос с несуществующим или неправильным параметром ID
        Response response = given()
                .when()
                .get("/offers/invalid-id")
                .then()
                .statusCode(400)  // проверка на статус 400
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 400) {
            logger.info("The test was successful: Received a response with code 400 (Bad Request)");
        }
    }

}