package wbd.tests.rest_assured;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import wbd.core.TestBaseRA;
import wbd.utils.DataProviders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;

@Epic("Security")
@Feature("SQL Injection Testing")
public class GetApiSecurityTests extends TestBaseRA {
    // Задача проверки:
    // Подставляем вредоносные строки в query-параметры (в category, priceFrom, location)
    //Проверяем:
    //нет 500/502/stack trace
    //нет SQL ошибок
    //сервис корректно фильтрует/отфутболивает
    // GET /offers/filter, SQL-инъекции в query-параметры
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
    @Severity(SeverityLevel.CRITICAL)
    @Story("SQL Injection in offer filter parameters")
    @Description("Verify that the system correctly handles SQL injection attempts in query parameters such as category, priceFrom, and location.")
    @TmsLink("")
    public void testSqlInjectionInOfferFilter(String maliciousInput) {
        given()
                .queryParam("category", maliciousInput)
                .queryParam("location", "1") // валидный ID, чтобы не мешал
                .queryParam("priceFrom", "0")
                .queryParam("priceTo", "9999")
                .when()
                .get("/offers/filter")
                .then()
                .statusCode(anyOf(is(200), is(400), is(422))) // главное — не 500+
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }

    // SQL-инъекции через path-параметр в GET /offers/{id}
    // Подставляет вредоносное значение в {id} оффера.
    // Проверяется, что:
    // не вылетает ошибка 500;
    // нет SQL сообщений;
    //бэк отвечает контролируемо (400, 404, 422 и т.д.)
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
    @Severity(SeverityLevel.CRITICAL)
    @Story("SQL Injection in offer ID parameter")
    @Description("Verify that the system correctly handles SQL injection attempts in the path parameter (offer ID).")
    @TmsLink("")
    public void testSqlInjectionInOfferId(String maliciousId) {
        given()
                .when()
                .get("/offers/" + maliciousId)
                .then()
                .statusCode(anyOf(is(400), is(403), is(404), is(422))) // корректное поведение: NOT FOUND, BAD REQUEST и т.п.
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }
    // баг-репорт QA-BugReport-10, сервер возвращает 200
    @Test(groups = "security")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Server error handling")
    @Description("Verify that the system correctly handles server errors (e.g., incorrect endpoint) and responds with appropriate status code.")
    @TmsLink("QA-BugReport-10")
    public void testServerError() {
        // симулируем ошибку на сервере, например, при вызове неправильного endpoint или когда сервер не может обработать запрос
        Response response = given()
                .when()
                .get("/offers/trigger-server-error")
                .then()
                .statusCode(404)  // проверка на статус 404
                .log().all()
                .extract().response();

        // статус 404
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected Not Found (404) error from the server");

        if (response.getStatusCode() == 404) {
            logger.info("Received expected 404 Not Found error response.");
        }
        softAssert.assertAll();
    }
}