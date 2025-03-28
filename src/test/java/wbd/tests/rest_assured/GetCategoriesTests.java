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
        Assert.assertEquals(statusCode, 200, "Код ответа должен быть 200");

        // проверяем, что список категорий не пуст
        List<String> categories = response.jsonPath().getList("$", String.class);
        Assert.assertFalse(categories.isEmpty(), "Список категорий не должен быть пустым");

        if (!categories.isEmpty()) {
            logger.info("---------------------------------------------------------");
            for (String category : categories) {
                logger.error("Category => " + category + ";");
            }
        }
        // проверяем, что все категории содержат непустые названия
        for (String category : categories) {
            Assert.assertNotNull(category, "Имя категории не должно быть null");
            Assert.assertFalse(category.trim().isEmpty(), "Имя категории не должно быть пустым");
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

        Assert.assertEquals(response.getStatusCode(), 404, "Код ответа должен быть 404");
    }
}
