package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.core.TestBaseRA;
import wbd.api.сlient.get.ApiClient_GetCategories;
import java.util.List;

public class GetCategoriesTests extends TestBaseRA {

    @Test
    public void testGetCategories_Success() {

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
                logger.error("Category => " + category + ";");
            }
        }
        // проверяем, что все категории содержат непустые названия
        for (String category : categories) {
            Assert.assertNotNull(category, "The category name must not be null");
            Assert.assertFalse(category.trim().isEmpty(), "The category name must not be empty");
        }
    }

    @Test
    public void testGetCategories_NotFound_404() {
        Response response = ApiClient_GetCategories.getCategories();
        int statusCode = response.getStatusCode();
        logger.info("Response status: " + statusCode);

        // если endpoint неправильный, то 404
        response = io.restassured.RestAssured
                .given()
                .when()
                .get("/categories-invalid")
                .then()
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 404, "The response code should be 404");
    }
}