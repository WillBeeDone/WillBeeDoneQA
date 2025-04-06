package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OfferRequestDto {
    private String title;
    private String description;
    private String categoryName;
    private double pricePerHour;
    private List<String> images;
}

