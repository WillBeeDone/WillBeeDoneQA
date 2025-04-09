package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProfileResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private LocationResponseDto locationDto;
}
