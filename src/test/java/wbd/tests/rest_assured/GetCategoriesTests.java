package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.utils.ApiClient_GetCategories;
import java.util.List;

public class GetCategoriesTests {

    @Test
    public void testGetCategories_Success() {
        Response response = ApiClient_GetCategories.getCategories();
        int statusCode = response.getStatusCode();
        System.out.println("Response status: " + statusCode);

        // статус 200
        Assert.assertEquals(statusCode, 200, "Код ответа должен быть 200");

        // проверяем, что список категорий не пуст
        List<String> categories = response.jsonPath().getList("$", String.class);
        Assert.assertFalse(categories.isEmpty(), "Список категорий не должен быть пустым");

        if (!categories.isEmpty()) {
            System.out.println("---------------------------------------------------------");
            for (String category : categories) {
                System.out.println("Category => " + category + ";");
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
        System.out.println("Response status: " + statusCode);

        // если endpoint неправильный, то 404
        response = io.restassured.RestAssured
                .given()
                .baseUri("http://localhost:8080/api")
                .when()
                .get("/categories-invalid")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 404, "Код ответа должен быть 404");
    }
}
