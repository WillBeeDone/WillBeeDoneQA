package wbd.api.client.get_post;

import io.restassured.response.Response;
import wbd.api.dto.AddOfferRequestDto;

import java.io.File;

import static io.restassured.RestAssured.given;

public class ApiClient_PostAddOffer {

    public static Response addOffer(AddOfferRequestDto requestDto, String accessToken) {
        var request = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("multipart/form-data")
                .multiPart("pricePerHour", requestDto.getPricePerHour())
                .multiPart("description", requestDto.getDescription())
                .multiPart("categoryName", requestDto.getCategoryName())
                .multiPart("title", requestDto.getTitle());

        // поле для изображений
        if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
            for (String imageName : requestDto.getImages()) {
                File file = new File("src/test/resources/images/" + imageName);
                if (file.exists()) {
                    request.multiPart("images", file);
                }
            }
        }

        return request
                .when()
                .post("/users/offers")
                .then()
                .extract()
                .response();
    }
}
