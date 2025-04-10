package wbd.tests.rest_assured;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get_post.ApiClient_GetOffers;
import wbd.api.dto.OfferDto;
import wbd.api.dto.OfferResponseDto;
import wbd.core.TestBaseRA;

import java.util.List;

// В тестах проверяем параметры запроса для получения списка офферов !
//  что фильтры работают корректно:
// - можно фильтровать по cityName, category, keyPhrase
// - можно ограничить по minPrice / maxPrice
// - работает sort=pricePerHour,desc
// - корректные size и page
@Epic("Offers")
@Feature("Get Offers with Parameters")
public class GetOffersWithParamsTests extends TestBaseRA {

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with cityName filter")
    @Description("Verify that offers are filtered correctly by cityName (e.g. Berlin).")
    @TmsLink("")    public void testGetOffersWithCityNameParam() {
        logger.info("Starting GetOffersWithCityNameParam test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметром cityName=Berlin
        Response response = ApiClient_GetOffers.getOffersWithParams("Berlin", null, null, null, null, null, null, null);
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // парсим ответ в объект OfferResponseDto
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);

        // проверяем, что список офферов не пустой
        List<OfferDto> offers = offerResponse.getContent();
        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "Offers list must not be empty");

        // проверяем, что город в каждом оффере соответствует фильтру
        for (OfferDto offer : offers) {
            softAssert.assertTrue(offer.getUserFilterResponseDto().getLocationDto().getCityName().contains("Berlin"),
                    "CityName should contain 'Berlin'");
        }

        softAssert.assertAll();
    }

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with category filter")
    @Description("Verify that offers are filtered correctly by category (e.g. Plumber).")
    @TmsLink("")
    public void testGetOffersWithCategoryParam() {
        logger.info("Starting GetOffersWithCategoryParam test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметром category=Plumber
        Response response = ApiClient_GetOffers.getOffersWithParams(null, "Plumber", null, null, null, null, null, null);
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // парсим ответ в объект OfferResponseDto
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);

        // проверяем, что список офферов не пустой
        List<OfferDto> offers = offerResponse.getContent();
        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "Offers list must not be empty");

        // проверяем, что категория каждого оффера соответствует фильтру
        for (OfferDto offer : offers) {
            softAssert.assertTrue(offer.getCategoryDto().getName().contains("Plumber"),
                    "Category should contain 'Plumber'");
        }

        softAssert.assertAll();
    }

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with keyPhrase filter")
    @Description("Verify that offers are filtered correctly by keyPhrase (e.g. Plumber).")
    @TmsLink("")
    public void testGetOffersWithKeyPhraseParam() {
        logger.info("Starting GetOffersWithKeyPhraseParam test");
        logger.info("=========================================");

        // отправляем GET-запрос с параметром keyPhrase=Plumber
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, "Plumber", null, null, null, null, null);
        logger.info("Response body: " + response.asString());

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // Парсим ответ в объект OfferResponseDto
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);

        // проверяем, что список офферов не пустой
        List<OfferDto> offers = offerResponse.getContent();
        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "Offers list must not be empty");

        // проверяем, что categoryDto.name соответствует фильтру
        for (OfferDto offer : offers) {
            String categoryName = offer.getCategoryDto().getName();
            softAssert.assertTrue(
                    categoryName != null && categoryName.toLowerCase().contains("plumber"),
                    "Offer category name should contain 'Plumber'"
            );
        }

        softAssert.assertAll();
    }

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with price range filter")
    @Description("Verify that offers are filtered correctly by price range.")
    @TmsLink("")
    public void testGetOffersWithPriceRangeParams() {
        logger.info("Starting GetOffersWithPriceRangeParams test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметрами minPrice=50 и maxPrice=200
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, 50.0, 200.0, null, null, null);
        logger.info("Response body: " + response.asString());

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // парсим ответ в объект OfferResponseDto
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);

        // проверяем, что список офферов не пустой
        List<OfferDto> offers = offerResponse.getContent();
        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "Offers list must not be empty");

        // проверяем, что цена каждого оффера находится в пределах заданного диапазона
        for (OfferDto offer : offers) {
            softAssert.assertTrue(offer.getPricePerHour() >= 50 && offer.getPricePerHour() <= 200,
                    "PricePerHour should be between 50 and 200");
        }

        softAssert.assertAll();
    }

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with sorting parameter")
    @Description("Verify that offers are sorted correctly by pricePerHour.")
    @TmsLink("")
    public void testGetOffersWithSortingParam() {
        logger.info("Starting GetOffersWithSortingParam test");
        logger.info("=============================================");

        //  GET-запрос с параметром сортировки
        Response response = ApiClient_GetOffers.getOffersWithParams(
                null, null, null, null, null,
                "pricePerHour,desc", // сортировка
                null, null
        );

        logger.info("Response body: " + response.asString());

        // проверяем статус-код 200 OK
        int statusCode = response.getStatusCode();
        softAssert.assertEquals(statusCode, 200, "Expected status code 200");

        // если ошибка — не продолжаем, выводим причину
        if (statusCode != 200) {
            logger.error("❌ Server returned error instead of expected data: " + response.asString());
            softAssert.fail("Can't parse OfferResponseDto because server returned error");
            softAssert.assertAll();
            return;
        }
        // парсим ответ
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);
        List<OfferDto> offers = offerResponse.getContent();

        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "Offers list must not be empty");

        // проверяем, что отсортировано по убыванию
        double previousPrice = Double.MAX_VALUE;
        for (OfferDto offer : offers) {
            double currentPrice = offer.getPricePerHour();
            softAssert.assertTrue(
                    currentPrice <= previousPrice,
                    String.format("Offers should be sorted by pricePerHour descending. Got: %.2f > %.2f", currentPrice, previousPrice)
            );
            previousPrice = currentPrice;
        }

        softAssert.assertAll();
    }

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with pagination parameters")
    @Description("Verify that offers pagination works with size and page parameters.")
    @TmsLink("")
    public void testGetOffersWithPaginationParams() {
        logger.info("Starting GetOffersWithPaginationParams test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметрами size=5 и page=1
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, 5, 1);
        logger.info("Response body: " + response.asString());

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // парсим ответ в объект OfferResponseDto
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);

        // проверяем параметры пагинации
        softAssert.assertEquals(offerResponse.getSize(), 5, "Expected size to be 5");
        softAssert.assertEquals(offerResponse.getNumber(), 1, "Expected page number to be 1");

        softAssert.assertAll();
    }

    // ========================== негативные тесты ==========================

    @Test(groups = "Negative")  // баг-репорт QA-BugReport - 23
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get offers with invalid cityName")
    @Description("Verify that the system returns an error for invalid cityName (e.g. NonExistentCity).")
    @TmsLink("QA-BugReport - 23")

    public void testGetOffersWithInvalidCityName() {
        logger.info("Testing GetOffers with Invalid City Name");

        // отправляем запрос с несуществующим городом
        Response response = ApiClient_GetOffers.getOffersWithParams("NonExistentCity", null, null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) или 404 (Not Found) для несуществующего города
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid cityName");

        // проверяем, что в ответе есть сообщение об ошибке (если оно присутствует)
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    // баг-репорт QA-BugReport - 22
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with invalid category filter")
    @Description("Verify the response when an invalid category is passed.")
    @TmsLink("QA-BugReport-23")
    public void testGetOffersWithInvalidCategory() {
        logger.info("Testing GetOffers with Invalid Category");

        // отправляем запрос с несуществующей категорией
        Response response = ApiClient_GetOffers.getOffersWithParams(null, "NonExistentCategory", null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) или 404 (Not Found) для несуществующей категории
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid category");

        // проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    // баг-репорт QA-Bagreport -24 (не проверяет валидность цен)
    @Test(groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get offers with invalid price filter")
    @Description("Check that system handles invalid price parameters correctly (bug QA-Bugreport-24).")
    @TmsLink("QA-Bugreport-24")
    public void testGetOffersWithInvalidPrice() {
        logger.info("Testing GetOffers with Invalid Price");

        // Здесь просто передаем null, чтобы тестировать, что происходит с отсутствием значения
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, null);

        // Проверяем, что статус ответа либо 400 (Bad Request), либо 422 (Unprocessable Entity)
        softAssert.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 422,
                "Expected status code 400 or 422 for invalid price, but found " + response.getStatusCode());

        // Проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    // баг-репорт QA-BagReport-25 (null не отбивает)
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with invalid minPrice filter")
    @Description("Verify behavior when minPrice is set to an invalid value (bug QA-Bugreport-25).")
    @TmsLink("QA-Bugreport-25")
    public void testGetOffersWithInvalidMinPrice() {
        logger.info("Testing GetOffers with Invalid Min Price");

        // отправляем запрос с некорректным значением для параметра minPrice (строка вместо числа)
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, 0.0, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) для некорректной цены
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid minPrice");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with invalid maxPrice filter")
    @Description("Verify behavior when maxPrice is set to a negative value.")
    @TmsLink("QA-Bugreport-26")
    public void testGetOffersWithInvalidMaxPrice() {
        logger.info("Testing GetOffers with Invalid Max Price");

        // отправляем запрос с некорректным значением для параметра maxPrice (строка вместо числа)
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, -5.0, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) для некорректной максимальной цены
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid maxPrice");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    // баг-репорт QA-BagReport -25
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with negative page number")
    @Description("Check system behavior when an invalid (negative) page number is passed (bug QA-Bugreport-25).")
    @TmsLink("QA-Bugreport-25")
    public void testGetOffersWithNegativePage() {
        logger.info("Testing GetOffers with Invalid Page Number");

        // отправляем запрос с отрицательным значением для параметра page
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, -1);

        // проверяем, что статус ответа 400 (Bad Request) для отрицательного номера страницы
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for negative page number");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("message");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    // баг-репорт QA-BagReport -27
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get offers with invalid sort parameter")
    @Description("Ensure that invalid sort parameters result in appropriate error (bug QA-Bugreport-27).")
    @TmsLink("QA-Bugreport-27")
    public void testGetOffersWithInvalidSort() {
        logger.info("Testing GetOffers with Invalid Sort Parameter");

        // отправляем запрос с неверным параметром sort
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, "invalidSort", null, null);

        // проверяем, что статус ответа 400 (Bad Request) для неверного параметра sort
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid sort parameter");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    // добавлено в баг-репорт QA-Bagreport -24
    @Test(groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get offers with missing mandatory parameters")
    @Description("Verify the behavior when mandatory parameters are missing (bug QA-Bugreport-24).")
    @TmsLink("QA-Bugreport-24")
    public void testGetOffersWithMissingMandatoryParams() {
        logger.info("Testing GetOffers with Missing Mandatory Parameters");

        // отправляем запрос без обязательных параметров (например, page и size)
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) для отсутствующих обязательных параметров
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for missing mandatory parameters");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }


}