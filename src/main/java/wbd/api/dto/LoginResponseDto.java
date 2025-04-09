package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LoginResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocationResponseDto locationDto;
    private String profilePicture;
    private List<RoleDto> roles;
    private boolean active;
    private boolean blocked;
    private String accessToken;
    private String refreshToken;
}
