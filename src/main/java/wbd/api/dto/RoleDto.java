package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleDto {
    private int id;
    private String title;
    private String authority;
}