package wbd.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AllOffersResponseDto {
    private int id;
    private String title;
    private CategoryResponseDto categoryResponseDto;
    private double pricePerHour;
    private String description;
    private UserDetailsDto userFilterResponseDto;

    @Getter
    @Setter
    @ToString
    public static class UserDetailsDto {
        private String firstName;
        private String lastName;
        private String profilePicture;
        private LocationResponseDto locationResponseDto;
    }
}