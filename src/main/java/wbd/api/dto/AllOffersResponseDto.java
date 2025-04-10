package wbd.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllOffersResponseDto {
    private int id;
    private String title;
    private CategoryResponseDto categoryDto;
    private double pricePerHour;
    private String description;
    private UserFilterResponseDto userFilterResponseDto;
}
