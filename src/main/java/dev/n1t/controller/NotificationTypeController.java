package dev.n1t.controller;

import dev.n1t.dto.NotificationTypeCreationDTO;
import dev.n1t.dto.NotificationTypeUpdateDTO;
import dev.n1t.model.NotificationType;
import dev.n1t.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificationType")
public class NotificationTypeController {
    private final NotificationService notificationService;

    public NotificationTypeController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationType> createNotificationType(@RequestBody NotificationTypeCreationDTO notificationTypeCreationDTO){
        NotificationType notificationType = notificationService.CreateNotificationType(notificationTypeCreationDTO);
        return new ResponseEntity<>(notificationType, HttpStatus.CREATED);
    }

    @GetMapping("/{typeName}")
    public ResponseEntity<NotificationType> getNotificationType(@PathVariable String typeName) {
        NotificationType notificationType = notificationService.getNotificationType(typeName);
        return ResponseEntity.ok(notificationType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationType(@PathVariable Long id) {
        notificationService.deleteNotificationType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationType> updateNotificationType(@PathVariable Long id, @RequestBody NotificationTypeUpdateDTO updateDTO) {
        NotificationType updatedNotificationType = notificationService.updateNotificationType(id, updateDTO);
        return ResponseEntity.ok(updatedNotificationType);
    }


}
