package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.dto.FilteredOffersResponseDto;
import wbd.utils.ApiClient_GetOfferById;

import static io.restassured.RestAssured.given;

public class GetOfferByIdTests {

    @Test
    public void testGetOfferById() {
        int offerId = 1;  // допустим, оффер с ID 1 существует

        Response response = ApiClient_GetOfferById.getOfferById(offerId); // получаем оффер по ID
        System.out.println("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // проверка статуса 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            System.out.println("Тест прошел успешно: Код ответа 200 (OK)");
        } else {
            System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode());
        }

        // парсим JSON в объект FilteredOffersResponseDto
        FilteredOffersResponseDto offer = response.as(FilteredOffersResponseDto.class);

        softAssert.assertNotNull(offer, "Offer не должен быть null");

        // все необходимые поля присутствуют
        softAssert.assertNotNull(offer.getTitle(), "Title не должен быть null");
        softAssert.assertTrue(offer.getTitle().length() > 0, "Title не должен быть пустым");

        softAssert.assertNotNull(offer.getDescription(), "Description не должен быть null");
        softAssert.assertTrue(offer.getDescription().length() > 0, "Description не должен быть пустым");

        softAssert.assertTrue(offer.getPricePerHour() > 0, "Цена должна быть положительной");

        // проверка categoryResponseDto как объекта
        softAssert.assertNotNull(offer.getCategoryResponseDto(), "Category не должен быть null");
        softAssert.assertNotNull(offer.getCategoryResponseDto().getName(), "Name категории не должен быть null");
        softAssert.assertTrue(offer.getCategoryResponseDto().getName().length() > 0, "Category name не должен быть пустым");

        // проверка UserFilterResponseDto (если оно есть)
        if (offer.getUserFilterResponseDto() != null) {
            softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getFirstName().length() > 0, "FirstName не должен быть пустым");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLastName(), "LastName не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getLastName().length() > 0, "LastName не должен быть пустым");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getProfilePicture(), "ProfilePicture не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getProfilePicture().length() > 0, "ProfilePicture не должен быть пустым");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationResponseDto(), "LocationResponseDto не должен быть null");

            if (offer.getUserFilterResponseDto().getLocationResponseDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationResponseDto().getCityName(), "CityName не должен быть null");
                softAssert.assertTrue(offer.getUserFilterResponseDto().getLocationResponseDto().getCityName().length() > 0, "CityName не должен быть пустым");
            }
        }

        // проверка на наличие хотя бы одного изображения
        if (offer.getImages() != null && !offer.getImages().isEmpty()) {
            softAssert.assertNotNull(offer.getImages().get(0).getImageUrl(), "ImageUrl не должен быть null");
            softAssert.assertTrue(offer.getImages().get(0).getImageUrl().length() > 0, "ImageUrl не должен быть пустым");
        }

        // запуск
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
            System.out.println("Ошибка: Получен ответ с кодом 404 (Not Found)");
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersByInvalidId_400() {
        // запрос с несуществующим или неправильным параметром ID
        Response response = given()
                .when()
                .get("/api/offers/invalid-id")
                .then()
                .statusCode(400)  // проверка на статус 400
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 400) {
            System.out.println("Тест прошел успешно: Получен ответ с кодом 400 (Bad Request)");
        }
    }

//    @Test
//    public void testServerError() {
//        // симулируем ошибку на сервере, например, при вызове неправильного endpoint или когда сервер не может обработать запрос
//        Response response = given()
//                .when()
//                .get("/api/offers/trigger-server-error")
//                .then()
//                .statusCode(500)  // проверка на статус 500
//                .log().all()
//                .extract().response();
//
//        if (response.getStatusCode() == 500) {
//            System.out.println("Ошибка: Получен ответ с кодом 500 (Internal Server Error)");
//        }
//    }


}