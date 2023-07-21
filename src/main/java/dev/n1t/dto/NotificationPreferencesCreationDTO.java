package dev.n1t.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationPreferencesCreationDTO {
    private Long userId;
    private Long NotificationTypeId;

    private Boolean enabled;
}
