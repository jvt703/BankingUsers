package dev.n1t.controller;

import dev.n1t.dto.NotificationPreferencesCreationDTO;
import dev.n1t.model.NotificationPreferences;
import dev.n1t.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificationPreference")
public class NotificationPreferenceController {

    private final NotificationService notificationService;

    public NotificationPreferenceController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<NotificationPreferences> createNotificationPreferences(@RequestBody NotificationPreferencesCreationDTO preferencesCreationDTO) {
        NotificationPreferences notificationPreferences = notificationService.createNotificationPreferences(preferencesCreationDTO);
        return ResponseEntity.ok(notificationPreferences);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationPreferences>> getAllNotificationPreferencesByUserId(@PathVariable Long userId) {
        List<NotificationPreferences> notificationPreferences = notificationService.getAllNotificationPreferencesByUserId(userId);
        return ResponseEntity.ok(notificationPreferences);
    }

    @PutMapping("/update/{userId}/{notificationTypeId}")
    public ResponseEntity<NotificationPreferences> updateNotificationPreferences(@PathVariable Long userId, @PathVariable Long notificationTypeId, @RequestBody boolean enabled) {
        NotificationPreferences updatedNotificationPreferences = notificationService.updateNotificationPreferences(userId, notificationTypeId, enabled);
        return ResponseEntity.ok(updatedNotificationPreferences);
    }




}
