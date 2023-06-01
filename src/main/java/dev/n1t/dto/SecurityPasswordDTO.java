package dev.n1t.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityPasswordDTO {
    private String securityAnswer;
    private String email;
}
