package wbd.api.сlient.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserFilterResponseDto {
    private String firstName;
    private String lastName;
    private String profilePicture;
    private LocationResponseDto locationDto;
}