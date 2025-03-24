package wbd.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FilteredOffersResponseDto {
    private int id;
    private String title;
    private String categoryResponseDto;
    private double pricePerHour;
    private String description;
    private List<ImageDto> images;
    private UserFilterResponseDto userFilterResponseDto;

    @Getter
    @Setter
    @ToString
    public static class ImageDto {
        private int id;
        private String imageUrl;
    }
}