package wbd.tests.rest_assured;


import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;

import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseRA;
import wbd.utils.DataProviders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;

@Epic("Security")
@Feature("SQL Injection")
@Listeners({AllureTestNg.class})

public class GetApiSecurityTests extends TestBaseRA {

    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
    @TmsLink("WBD_48")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /offers/filter")
    @Description("SQL defense protection check in the OfferFilter parameter")

    public void testSqlInjectionInOfferFilter(String maliciousInput) {
        given()
                .queryParam("category", maliciousInput)
                .queryParam("location", "1")
                .queryParam("priceFrom", "0")
                .queryParam("priceTo", "9999")
                .when()
                .get("/offers/filter")
                .then()
                .statusCode(anyOf(is(200), is(400), is(422)))
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }

    @Test(dataProvider = "sqlInjectionPayloads", dataProviderClass = DataProviders.class, groups = "security")
    @TmsLink("WBD_47")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /offers/{id}")
    @Description("Checking protection against SQL injections in the Offer ID: /Offers /{ID}")
public void testSqlInjectionInOfferId(String maliciousId) {

        given()
                .when()
                .get("/offers/" + maliciousId)
                .then()
                .statusCode(anyOf(is(400), is(403), is(404), is(422)))
                .body(not(containsString("SQL")))
                .body(not(containsString("syntax")))
                .body(not(containsString("exception")))
                .log().ifValidationFails();
    }


    @Test(groups = "security")
    @TmsLink("WBD_51")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /offers/trigger-server-error")
    @Description("Error testing 404 with incorrect Endpoint: /Offers /Trigger-Server-Serror")
    public void testServerErrorTest() {
        // симулируем ошибку на сервере, например, при вызове неправильного endpoint или когда сервер не может обработать запрос
        Response response = given()
                .when()
                .get("/offers/trigger-server-error")
                .then()
                .statusCode(404)
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 404) {
            System.out.println("Error: A response with the code 404 (Internet Server Error) was received");
        }
    }
}
