package wbd.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddOfferRequestDto {
    private double pricePerHour;
    private String description;
    private String categoryName;
    private String title;
    private List<String> images;
}

