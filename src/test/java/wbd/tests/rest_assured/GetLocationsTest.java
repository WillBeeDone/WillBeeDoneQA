package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get_post.ApiClient_GetLocations;
import wbd.core.TestBaseRA;
import wbd.api.dto.LocationResponseDto;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GetLocationsTest extends TestBaseRA {

    @Test
    public void testGetLocations() {

        logger.info("Start testing GetLocations");
        logger.info("=============================================");

        // запрос на получение локаций
        Response response = ApiClient_GetLocations.getLocations();
        logger.info("Response body: " + response.asString());

        // проверяем, что статус ответа 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // парсим JSON как список строк, т.к. ответ выглядит как JSON-массив строк
        List<String> cityNames = response.jsonPath().getList("", String.class);
        softAssert.assertNotNull(cityNames, "City names list should not be null");
        softAssert.assertFalse(cityNames.isEmpty(), "City names list should not be empty");

        //  список строк -> в список DTO для проверок
        List<LocationResponseDto> locations = new ArrayList<>();
        for (String city : cityNames) {
            LocationResponseDto dto = new LocationResponseDto();
            dto.setCityName(city);
            locations.add(dto);
        }

        // Проверяем, что каждое значение не пустое
        for (LocationResponseDto loc : locations) {
            softAssert.assertNotNull(loc.getCityName(), "City name should not be null");
            softAssert.assertTrue(!loc.getCityName().trim().isEmpty(), "City name should not be empty");
        }

        softAssert.assertAll();
    }

    // ==================== негативные тесты =====================================

    @Test
    public void testGetLocationsWithInvalidEndpoint_Returns404() {
        // GET-запрос на неверный эндпоинт
        Response response = given()
                .when()
                .get("/locations/invalid") // неправильный путь
                .then()
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid endpoint /locations/invalid");
        logger.info("Received status code " + response.getStatusCode() + " for GET request to /locations/invalid");
        softAssert.assertAll();
    }

    // баг-репорт QA-BugReport - 11, не возвращает 400, а отвечает 200 (ОК)
    @Test
    public void testGetLocationsWithUnexpectedQueryParam_Returns400() {
        //  GET-запрос с неподдерживаемым query-параметром
        Response response = given()
                .queryParam("unexpected", "value") // неожиданный query-параметр
                .when()
                .get("/locations")
                .then()
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();
        // ожидается, что API отклонит запрос с неизвестным параметром, вернув 400 Bad Request
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for GET request to /locations with unexpected query param");
        logger.info("Received status code " + response.getStatusCode() + " for GET request to /locations with unexpected query param");
        softAssert.assertAll();
    }
}