package wbd.api.client.get;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetOffers {

    //  метод для получения всех офферов
    public static Response getOffers() {
        return RestAssured
                .given()
                .when()
                .get("/offers")
                .then()
                .log().all()
                .extract()
                .response();
    }

    // метод для получения офферов с параметрами фильтрации
    public static Response getOffersWithParams(String cityName, String category, String keyPhrase,
                                               Double minPrice, Double maxPrice, String sort, Integer size, Integer page) {

        // Создаем запрос с базовыми параметрами
        var request = RestAssured.given();

        // добавляем параметры фильтрации в запрос, если они не равны null
        if (cityName != null) request.param("cityName", cityName);
        if (category != null) request.param("category", category);
        if (keyPhrase != null) request.param("keyPhrase", keyPhrase);
        if (minPrice != null) request.param("minPrice", minPrice);
        if (maxPrice != null) request.param("maxPrice", maxPrice);
        if (sort != null) request.param("sort", sort);
        if (size != null) request.param("size", size);
        if (page != null) request.param("page", page);

        // отправляем GET-запрос с параметрами и возвращаем ответ
        return request
                .when()
                .get("/offers")
                .then()
                .log().all()
                .extract()
                .response();
    }
}