package wbd.api.client.get;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetOffers {

    // Статический метод для получения всех офферов
    public static Response getOffers() {
        return RestAssured
                .given()
                .when()
                .get("/offers")  // Обрати внимание, что "/offers" может быть другим путем в зависимости от настроек твоего API
                .then()
                .log().all()
                .extract()
                .response();
    }
}
