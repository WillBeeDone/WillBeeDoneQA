package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wbd.utils.ApiClient_GetFilterOffers;

public class FirstGetTests {
    @Test
    public void testGetFilteredOffers() {
        Response response = ApiClient_GetFilterOffers.getFilteredOffers();

        // статус код
        Assertions.assertEquals(200, response.getStatusCode(), "Статус-код не 200");

    }
}
