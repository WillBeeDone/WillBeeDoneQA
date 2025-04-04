package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AllOffersResponseDto {
    private int id;
    private String title;
    private CategoryResponseDto categoryDto;
    private double pricePerHour;
    private String description;
    private UserDetailsDto userFilterResponseDto;
}
