package dev.n1t.repository;

import dev.n1t.model.NotificationPreferences;
import dev.n1t.model.NotificationType;
import dev.n1t.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, Long> {
    List<NotificationPreferences> findAllByUser(User user);
    Optional<NotificationPreferences> findByUserAndNotificationType(User user, NotificationType notificationType);
}
