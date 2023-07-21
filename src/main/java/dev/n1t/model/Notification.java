package dev.n1t.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @NotNull
    @Column(nullable = false, name = "dateTimeSent")
    private Long dateTimeSent;

    @NotBlank
    @Column(nullable = false)
    private String message;

    @NotNull
    @ManyToOne()
    @JoinColumn(nullable = false, name = "notificationTypeId", referencedColumnName = "id")
    private NotificationType notificationType;
}
