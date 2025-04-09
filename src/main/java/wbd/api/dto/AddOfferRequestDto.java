package wbd.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class AddOfferRequestDto {
    private double pricePerHour;
    private String description;
    private String categoryName;
    private String title;
    private List<String> images;
}

