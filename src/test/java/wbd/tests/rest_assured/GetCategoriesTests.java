package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.api.client.CategoriesClient;
import wbd.core.TestBaseRA;

import java.util.List;
import static io.restassured.RestAssured.given;

public class GetCategoriesTests extends TestBaseRA {
    private static final Logger logger = LoggerFactory.getLogger(GetCategoriesTests.class);
    @Test
    public void testGetCategories_Success() {
        Response response = CategoriesClient.getCategories();
        int statusCode = response.getStatusCode();
        logger.info("Response status: {}", statusCode);

        // статус 200
        Assert.assertEquals(statusCode, 200, "Код ответа должен быть 200");

        // проверяем, что список категорий не пуст
        List<String> categories = response.jsonPath().getList("$", String.class);
        Assert.assertFalse(categories.isEmpty(), "Список категорий не должен быть пустым");

        if (!categories.isEmpty()) {
            logger.info("---------------------------------------------------------");
            for (String category : categories) {
                logger.info("Category => {}", category);
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

        // если endpoint неправильный, то 404
        Response response = given()
                .when()
                .get("/categories-invalid")
                .then()
                .extract()
                .response();

        int statusCode = response.getStatusCode();
        logger.info("Response status: {}", statusCode);

        Assert.assertEquals(response.getStatusCode(), 404, "Код ответа должен быть 404");
    }
}
