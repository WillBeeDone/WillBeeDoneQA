package wbd.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequestDto {
    private String email;
    private String password;
}
