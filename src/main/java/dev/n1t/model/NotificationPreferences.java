package dev.n1t.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NotificationPreferences")
public class NotificationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(nullable = false, name = "notificationTypeId", referencedColumnName = "id")
    private NotificationType notificationType;

    @NotNull(message = "Enabled status cannot be null")
    private boolean enabled;
}
