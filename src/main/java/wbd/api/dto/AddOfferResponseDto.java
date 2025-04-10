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
public class AddOfferResponseDto {
    private int id;
    private String title;
    private double pricePerHour;
    private String description;
    private CategoryResponseDto category;
    private List<String> images;
    private Boolean active;
}
