package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetOffers;
import wbd.api.dto.OfferDto;

import java.util.List;

public class GetOffersTests {

    @Test
    public void testGetOffers() {

        // Логирование начала теста
        System.out.println("Start testing GetOffers");
        System.out.println("=============================================");

        // Получаем офферы с API
        Response response = ApiClient_GetOffers.getOffers();
        System.out.println("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // Проверяем статус 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // Если статус 200, продолжаем тестировать
        if (response.getStatusCode() == 200) {
            System.out.println("The test was successful: Response code 200 (OK)");
        } else {
            System.err.println("Error: Received response with code " + response.getStatusCode());
        }

        // Парсим JSON в список объектов OfferDto
        List<OfferDto> offers = response.jsonPath().getList("content", OfferDto.class);

        // Проверка, что список офферов не пуст
        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertTrue(!offers.isEmpty(), "The Offers list must not be empty");

        // Проверяем каждый оффер
        if (!offers.isEmpty()) {
            OfferDto offer = offers.get(0);  // Проверка первого оффера

            // Проверяем поля оффера
            softAssert.assertNotNull(offer.getTitle(), "Title must not be null");
            softAssert.assertTrue(!offer.getTitle().isEmpty(), "Title should not be empty");

            softAssert.assertNotNull(offer.getDescription(), "Description must not be null");
            softAssert.assertTrue(!offer.getDescription().isEmpty(), "Description should not be empty");

            softAssert.assertTrue(offer.getPricePerHour() > 0, "The price per hour must be positive");

            softAssert.assertNotNull(offer.getCategoryDto(), "Category must not be null");
            softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category name must not be null");

            // Проверка изображений
            List<ImageDto> images = offer.getImages();
            softAssert.assertNotNull(images, "Images list must not be null");
            softAssert.assertTrue(!images.isEmpty(), "Images list must not be empty");

            // Проверяем каждый объект изображения
            for (ImageDto image : images) {
                softAssert.assertNotNull(image.getImageUrl(), "Image URL must not be null");
                softAssert.assertTrue(!image.getImageUrl().isEmpty(), "Image URL should not be empty");
            }

            // Логируем информацию о каждом оффере и изображениях
            for (OfferDto o : offers) {
                System.out.println("Offer: " + o.getTitle() + ";");
                for (ImageDto img : o.getImages()) {
                    System.out.println("Image URL: " + img.getImageUrl());
                }
            }
        }
        softAssert.assertAll();
    }
}
