package dev.n1t.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDTO {

    private long id;
    private String updatedPassword;

    private String resetToken;
}
