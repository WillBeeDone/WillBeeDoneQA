package wbd.tests.rest_assured;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import wbd.core.TestBaseRA;
import wbd.utils.DataProviders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.Listeners;

@Epic("Security")
@Feature("SQL Injection")

@Listeners({AllureTestNg.class})
public class PostApiSecurityTests extends TestBaseRA {

    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
    @TmsLink("WBD_49")
    @Severity(SeverityLevel.CRITICAL)
    @Story("POST /auth/login")
    @Description("Checking protection against SQL Injections in the Email field during authorization")
    public void testSqlInjectionInLogin(String maliciousEmail) {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + maliciousEmail + "\", \"password\": \"anything\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(not(200))
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }

    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
    @TmsLink("WBD_50")
    @Severity(SeverityLevel.CRITICAL)
    @Story("POST /register")
    @Description("Checking protection from SQL Injections in the Email field during registration")
    public void testSqlInjectionInRegister(String maliciousEmail) {
        given()
                .contentType("application/json")
                .body("{\"email\": \"" + maliciousEmail + "\", \"password\": \"ValidPass123\"}")
                .when()
                .post("/register")
                .then()
                .statusCode(not(200))
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }
}
