package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.api.client.get_post.ApiClient_GetCategories;
import wbd.core.TestBaseRA;
import java.util.List;

public class GetCategoriesTests extends TestBaseRA {

    @Test
    public void GetCategories_PositiveTest() {

        logger.info("Start testing GetCategories");
        logger.info("=============================================");

        Response response = ApiClient_GetCategories.getCategories();
        int statusCode = response.getStatusCode();
        logger.info("Response status: " + statusCode);

        // статус 200
        Assert.assertEquals(statusCode, 200, "The response code must be 200");

        // проверяем, что список категорий не пуст
        List<String> categories = response.jsonPath().getList("$", String.class);
        Assert.assertFalse(categories.isEmpty(), "The list of categories should not be empty");

        if (!categories.isEmpty()) {
            logger.info("---------------------------------------------------------");
            for (String category : categories) {
                logger.info("Category => " + category + ";");
            }
        }
        // проверяем, что все категории содержат непустые названия
        for (String category : categories) {
            Assert.assertNotNull(category, "The category name must not be null");
            Assert.assertFalse(category.trim().isEmpty(), "The category name must not be empty");
        }
    }

    // ======================= негативные тесты ================================

    @Test
    public void testGetCategories_NotFound_404() {
        logger.info("Start testing GetCategories with invalid endpoint");
        logger.info("=============================================");

        // Отправляем запрос на несуществующий эндпоинт
        Response response = io.restassured.RestAssured
                .given()
                .when()
                .get("/categories-invalid")
                .then()
                .log().all()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        logger.info("Response status: " + statusCode);

        Assert.assertEquals(statusCode, 404, "The response code should be 404");
    }
}