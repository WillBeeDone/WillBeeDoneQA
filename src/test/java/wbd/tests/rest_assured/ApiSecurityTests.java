package wbd.tests.rest_assured;



import io.restassured.response.Response;
import org.testng.annotations.Test;
import wbd.core.TestBaseRA;

import static io.restassured.RestAssured.given;


public class ApiSecurityTests extends TestBaseRA {

    @Test // написать баг-репорт
    public void testServerError() {
        // симулируем ошибку на сервере, например, при вызове неправильного endpoint или когда сервер не может обработать запрос
        Response response = given()
                .when()
                .get("/api/offers/trigger-server-error")
                .then()
                .statusCode(500)  // проверка на статус 500
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 500) {
            System.out.println("Ошибка: Получен ответ с кодом 500 (Internal Server Error)");
        }
    }


}
