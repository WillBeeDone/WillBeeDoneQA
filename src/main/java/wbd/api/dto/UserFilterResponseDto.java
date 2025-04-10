package wbd.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFilterResponseDto {
    private String firstName;
    private String lastName;
    private String profilePicture;
    private LocationResponseDto locationDto;
}
