package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AddOfferResponseDto {
    private int id;
    private String title;
    private double pricePerHour;
    private String description;
    private CategoryResponseDto category;
    private List<String> images;
    private Boolean active;
}
