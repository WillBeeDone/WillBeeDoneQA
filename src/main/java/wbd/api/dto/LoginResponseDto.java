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
