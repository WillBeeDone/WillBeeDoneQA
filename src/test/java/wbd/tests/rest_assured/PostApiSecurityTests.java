package wbd.tests.rest_assured;

import org.testng.annotations.Test;
import wbd.core.TestBaseRA;
import wbd.utils.DataProviders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;

public class PostApiSecurityTests extends TestBaseRA {

    // Используя данные из DataProviders.
    // Проверяем защиту от SQL-инъекций через поле email в логине
    // При неудаче — выводим тело ответа
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
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
    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
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
}