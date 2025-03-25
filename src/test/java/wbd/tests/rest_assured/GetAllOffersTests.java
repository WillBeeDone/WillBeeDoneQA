package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.dto.AllOffersResponseDto;
import wbd.utils.ApiClient_GetAllOffers;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllOffersTests {

    @Test
    public void testGetAllOffers() {
        // получаем все офферы
        Response response = ApiClient_GetAllOffers.getAllOffers();
        System.out.println("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // статус 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            System.out.println("Тест прошел успешно: Код ответа 200 (OK)");
        } else {
            System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode());
        }

        // парсим JSON в список объектов AllOffersResponseDto
        List<AllOffersResponseDto> offers = response.jsonPath().getList("", AllOffersResponseDto.class);

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

            System.out.println("---------------------------------------------------------");
            for (AllOffersResponseDto offerDto : offers ) {
                System.out.println("Offer => " + offerDto.getTitle() + ";");
            }
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetAllOffersWithInvalidEndpoint_404() {
        Response response = given()
                .when()
                .get("/api/invalid-offers") // неправильный эндпоинт
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        //  статус 404
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid endpoint");

        System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode());

        softAssert.assertAll();
    }

    @Test   // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_400() {
        Response response = given()
                .queryParam("pricePerHour", "invalid-price") // некорректный параметр
                .when()
                .get("/api/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // статус 400
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid query param");

        System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode());

        softAssert.assertAll();
    }

    @Test  // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_Category_400() {
        Response response = given()
                .queryParam("category", "nonexistent-category") // некорректное значение категории
                .when()
                .get("/api/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "category"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'category' query param");

        System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode() + " при передаче некорректной категории");

        softAssert.assertAll();
    }

    @Test  // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_UserFirstName_400() {
        Response response = given()
                .queryParam("firstName", "") // Пустое значение для имени пользователя
                .when()
                .get("/api/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "firstName"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'firstName' query param");

        System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode() + " при передаче пустого имени пользователя");

        softAssert.assertAll();
    }

    @Test  // не проходит - баг репорт
    public void testGetAllOffersWithInvalidQueryParam_Location_400() {
        Response response = given()
                .queryParam("cityName", 123) // Некорректное значение для города
                .when()
                .get("/api/offers")
                .then()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Ожидаем статус 400 для некорректного параметра "cityName"
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid 'cityName' query param");

        System.out.println("Ошибка: Получен ответ с кодом " + response.getStatusCode() + " при передаче некорректного названия города");

        softAssert.assertAll();
    }


}

