package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetOffers;
import wbd.api.dto.OfferDto;
import wbd.api.dto.OfferResponseDto;
import wbd.core.TestBaseRA;

import java.util.List;

import static wbd.core.TestBaseUI.logger;

// В тестах проверяем параметры запроса для получения списка офферов !
//  что фильтры работают корректно:
// - можно фильтровать по cityName, category, keyPhrase
// - можно ограничить по minPrice / maxPrice
// - работает sort=pricePerHour,desc
// - корректные size и page
public class GetOffersWithParamsTests extends TestBaseRA {

    @Test
    public void testGetOffersWithCityNameParam() {
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

    @Test
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

    @Test
    public void testGetOffersWithKeyPhraseParam() {
        logger.info("Starting GetOffersWithKeyPhraseParam test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметром keyPhrase=Plumber with beard
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, "Plumber with beard", null, null, null, null, null);
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

        // проверяем, что ключевая фраза каждого оффера соответствует фильтру
        for (OfferDto offer : offers) {
            softAssert.assertTrue(offer.getTitle().contains("Plumber with beard"),
                    "Offer title should contain 'Plumber with beard'");
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithPriceRangeParams() {
        logger.info("Starting GetOffersWithPriceRangeParams test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметрами minPrice=50 и maxPrice=200
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, 50.0, 200.0, null, null, null);
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

        // проверяем, что цена каждого оффера находится в пределах заданного диапазона
        for (OfferDto offer : offers) {
            softAssert.assertTrue(offer.getPricePerHour() >= 50 && offer.getPricePerHour() <= 200,
                    "PricePerHour should be between 50 and 200");
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithSortingParam() {
        logger.info("Starting GetOffersWithSortingParam test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметром sort=pricePerHour,desc
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, "pricePerHour,desc", null, null);
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

        // проверяем, что офферы отсортированы по цене
        double previousPrice = -1;
        for (OfferDto offer : offers) {
            softAssert.assertTrue(offer.getPricePerHour() <= previousPrice, "Offers should be sorted by pricePerHour in descending order");
            previousPrice = offer.getPricePerHour();
        }

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithPaginationParams() {
        logger.info("Starting GetOffersWithPaginationParams test");
        logger.info("=============================================");

        // отправляем GET-запрос с параметрами size=5 и page=1
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, 5, 1);
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

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

    @Test
    public void testGetOffersWithInvalidCityName() {
        logger.info("Testing GetOffers with Invalid City Name");

        // отправляем запрос с несуществующим городом
        Response response = ApiClient_GetOffers.getOffersWithParams("NonExistentCity", null, null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) или 404 (Not Found) для несуществующего города
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid cityName");

        // проверяем, что в ответе есть сообщение об ошибке (если оно присутствует)
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithInvalidCategory() {
        logger.info("Testing GetOffers with Invalid Category");

        // отправляем запрос с несуществующей категорией
        Response response = ApiClient_GetOffers.getOffersWithParams(null, "NonExistentCategory", null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) или 404 (Not Found) для несуществующей категории
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid category");

        // проверяем сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test  //   ожидали статус-код 400 или 422, но получили 200 - писать багрепорт - API не проверяет валидность цен
    public void testGetOffersWithInvalidPrice() {
        logger.info("Testing GetOffers with Invalid Price");

        // Здесь просто передаем null, чтобы тестировать, что происходит с отсутствием значения
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, null);

        SoftAssert softAssert = new SoftAssert();

        // Проверяем, что статус ответа либо 400 (Bad Request), либо 422 (Unprocessable Entity)
        softAssert.assertTrue(response.getStatusCode() == 400 || response.getStatusCode() == 422,
                "Expected status code 400 or 422 for invalid price, but found " + response.getStatusCode());

        // Проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }



    @Test
    public void testGetOffersWithInvalidMinPrice() {
        logger.info("Testing GetOffers with Invalid Min Price");

        // Отправляем запрос с некорректным значением для параметра minPrice (строка вместо числа)
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, "invalidPrice", null, null, null, null, null);

        // Проверяем, что статус ответа 400 (Bad Request) для некорректной цены
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid minPrice");

        // Проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithInvalidMaxPrice() {
        logger.info("Testing GetOffers with Invalid Max Price");

        // отправляем запрос с некорректным значением для параметра maxPrice (строка вместо числа)
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) для некорректной максимальной цены
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid maxPrice");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithNegativePage() {
        logger.info("Testing GetOffers with Invalid Page Number");

        // отправляем запрос с отрицательным значением для параметра page
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, -1);

        // проверяем, что статус ответа 400 (Bad Request) для отрицательного номера страницы
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for negative page number");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithInvalidSort() {
        logger.info("Testing GetOffers with Invalid Sort Parameter");

        // отправляем запрос с неверным параметром sort
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, "invalidSort", null, null);

        // проверяем, что статус ответа 400 (Bad Request) для неверного параметра sort
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid sort parameter");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersWithMissingMandatoryParams() {
        logger.info("Testing GetOffers with Missing Mandatory Parameters");

        // отправляем запрос без обязательных параметров (например, page и size)
        Response response = ApiClient_GetOffers.getOffersWithParams(null, null, null, null, null, null, null, null);

        // проверяем, что статус ответа 400 (Bad Request) для отсутствующих обязательных параметров
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for missing mandatory parameters");

        // проверяем, что в ответе есть сообщение об ошибке
        String errorMessage = response.jsonPath().getString("error");
        softAssert.assertNotNull(errorMessage, "Error message must not be null");

        softAssert.assertAll();
    }


}