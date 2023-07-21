package dev.n1t.service;

import dev.n1t.dto.NotificationPreferencesCreationDTO;
import dev.n1t.dto.NotificationTypeCreationDTO;
import dev.n1t.dto.NotificationTypeUpdateDTO;
import dev.n1t.model.Notification;
import dev.n1t.model.NotificationPreferences;
import dev.n1t.model.NotificationType;
import dev.n1t.model.User;
import dev.n1t.repository.NotificationPreferencesRepository;
import dev.n1t.repository.NotificationRepository;
import dev.n1t.repository.NotificationTypeRepository;
import dev.n1t.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(
            NotificationRepository notificationRepository,
            NotificationPreferencesRepository notificationPreferencesRepository,
            NotificationTypeRepository notificationTypeRepository,
            UserRepository userRepository
    ){
        this.notificationRepository = notificationRepository;
        this.notificationPreferencesRepository= notificationPreferencesRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.userRepository= userRepository;
    }

    public NotificationType CreateNotificationType(NotificationTypeCreationDTO notificationTypeCreationDTO){
        NotificationType notificationType = NotificationType.builder()
                .typeName(notificationTypeCreationDTO.getTypeName())
                .description(notificationTypeCreationDTO.getDescription())
                .build();
        NotificationType notificationType1 = notificationTypeRepository.save(notificationType);
        return notificationType1;
    }


    public NotificationType getNotificationType(String typeName) {
        // Find the existing NotificationType in the repository based on the typeName
        Optional<NotificationType> optionalNotificationType = notificationTypeRepository.findByTypeName(typeName);
        if (optionalNotificationType.isPresent()) {
            return optionalNotificationType.get();
        } else {
            // Handle the case when the NotificationType with the provided typeName does not exist
            // You can throw an exception, return null, or handle it based on your application's requirements
            // For simplicity, let's throw a custom exception here:
            throw new RuntimeException("NotificationType with typeName '" + typeName + "' not found.");
        }
    }

    public void deleteNotificationType(Long id) {
        Optional<NotificationType> optionalNotificationType = notificationTypeRepository.findById(id);
        if (optionalNotificationType.isPresent()) {
            notificationTypeRepository.deleteById(id);
        } else {
            throw new RuntimeException("NotificationType with ID " + id + " not found.");
        }
    }
    public NotificationType updateNotificationType(Long id, NotificationTypeUpdateDTO updateDTO) {
        Optional<NotificationType> optionalNotificationType = notificationTypeRepository.findById(id);
        if (optionalNotificationType.isPresent()) {
            NotificationType notificationType = optionalNotificationType.get();
            notificationType.setDescription(updateDTO.getDescription());
            return notificationTypeRepository.save(notificationType);
        } else {
            throw new RuntimeException("NotificationType with ID " + id + " not found.");
        }
    }


    public NotificationPreferences createNotificationPreferences(NotificationPreferencesCreationDTO preferencesCreationDTO) {
        // Find the User and NotificationType based on provided IDs
        Optional<User> optionalUser = userRepository.findById(preferencesCreationDTO.getUserId());
        Optional<NotificationType> optionalNotificationType = notificationTypeRepository.findById(preferencesCreationDTO.getNotificationTypeId());

        if (optionalUser.isPresent() && optionalNotificationType.isPresent()) {
            User user = optionalUser.get();
            NotificationType notificationType = optionalNotificationType.get();

            // Create the NotificationPreferences
            NotificationPreferences notificationPreferences = NotificationPreferences.builder()
                    .user(user)
                    .notificationType(notificationType)
                    .enabled(preferencesCreationDTO.getEnabled())
                    .build();

            return notificationPreferencesRepository.save(notificationPreferences);
        } else {
            // Handle the case when User or NotificationType is not found
            throw new RuntimeException("User or NotificationType not found for the provided IDs.");
        }
    }
    public List<NotificationPreferences> getAllNotificationPreferencesByUserId(Long userId) {
        // Find the User based on provided ID
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Get all NotificationPreferences for the given User
            return notificationPreferencesRepository.findAllByUser(user);
        } else {
            throw new RuntimeException("User not found for the provided ID.");
        }
    }
    public NotificationPreferences updateNotificationPreferences(Long userId, Long notificationTypeId, boolean enabled) {
        // Find the User and NotificationType based on provided IDs
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<NotificationType> optionalNotificationType = notificationTypeRepository.findById(notificationTypeId);

        if (optionalUser.isPresent() && optionalNotificationType.isPresent()) {
            User user = optionalUser.get();
            NotificationType notificationType = optionalNotificationType.get();

            // Get the NotificationPreferences
            NotificationPreferences notificationPreferences = notificationPreferencesRepository.findByUserAndNotificationType(user, notificationType)
                    .orElseThrow(() -> new RuntimeException("NotificationPreferences not found for the provided IDs."));

            // Update the enabled status
            notificationPreferences.setEnabled(enabled);
            return notificationPreferencesRepository.save(notificationPreferences);
        } else {
            throw new RuntimeException("User or NotificationType not found for the provided IDs.");
        }
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification getNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification with id " + id + " not found"));
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }





}
