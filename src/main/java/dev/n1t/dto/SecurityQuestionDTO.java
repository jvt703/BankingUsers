package dev.n1t.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecurityQuestionDTO {
    private String email;
    private String securityQuestion;
}
