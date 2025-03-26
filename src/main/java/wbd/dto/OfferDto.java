package wbd.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfferDto {
    private int id;
    private String title;
    private CategoryResponseDto categoryResponseDto;
    private double pricePerHour;
    private String description;
    private UserFilterResponseDto userFilterResponseDto;
}