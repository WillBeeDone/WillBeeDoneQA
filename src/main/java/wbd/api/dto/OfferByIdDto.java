package wbd.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferByIdDto {
    private int id;
    private String title;
    private CategoryResponseDto categoryDto;
    private double pricePerHour;
    private String description;
    private List<ImageDto> images;  // список изображений
    private UserProfileResponseDto userProfileResponseDto;

    @Getter
    @Setter
    @ToString
    public static class ImageDto {
        private int id;
        private String imageUrl;
    }
}