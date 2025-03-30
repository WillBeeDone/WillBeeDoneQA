package wbd.tests.rest_assured;



import io.restassured.response.Response;
import org.testng.annotations.Test;
import wbd.core.TestBaseRA;
import wbd.web.utils.DataProviders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;


public class ApiSecurityTests extends TestBaseRA {

    // Используя данные из DataProviders.
    // Проверяем защиту от SQL-инъекций через поле email в логине
    // При неудаче — выводим тело ответа
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class)
    public void testSqlInjectionInLogin(String maliciousEmail) {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + maliciousEmail + "\", \"password\": \"anything\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(not(200)) // Ожидаем, что вход не удался
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }
    // проверка попыток SQL-инъекций при регистрации
    // используется ВАЛИДНЫЙ password для проверки поля email
    // В ожиданиях ответы 400,422. 403 statusCode НО не 200
    // 500 ответа быть не должно!
    // трассировки SQL/исключений быть не должно!
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class)
    public void testSqlInjectionInRegister(String maliciousEmail) {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + maliciousEmail + "\", \"password\": \"ValidPass123\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(not(200)) // Регистрация не должна пройти
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }
    // Задача проверки:
    // Подставляем вредоносные строки в query-параметры (в category, priceFrom, location)
    //Проверяем:
    //нет 500/502/stack trace
    //нет SQL ошибок
    //сервис корректно фильтрует/отфутболивает
    // GET /offers/filter, SQL-инъекции в query-параметры
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class)
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
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class)
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
