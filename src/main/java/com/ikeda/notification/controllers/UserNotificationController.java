package com.ikeda.notification.controllers;

import com.ikeda.notification.configs.security.AuthenticationCurrentUserService;
import com.ikeda.notification.configs.security.UserDetailsImpl;
import com.ikeda.notification.dtos.NotificationRecordDto;
import com.ikeda.notification.models.NotificationModel;
import com.ikeda.notification.services.NotificationService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserNotificationController {

    Logger logger = LogManager.getLogger(UserNotificationController.class);

    final NotificationService notificationService;
    final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserNotificationController(NotificationService notificationService, AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.notificationService = notificationService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(@PathVariable(value = "userId") UUID userId,
                                                                            Pageable pageable){
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrrentUser();
        logger.info(String.format("Authentication userId {%s} - getAllNotificationsByUser do userId {%s}", userDetails.getUserId(), userId));

        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))){
            return ResponseEntity.status(HttpStatus.OK).body(notificationService.findAllNotificationsByUser(userId, pageable));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/users/{userId}/notification/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable(value = "userId") UUID userId,
                                                     @PathVariable(value = "notificationId") UUID notificationId,
                                                     @RequestBody @Valid NotificationRecordDto notificationRecordDto) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrrentUser();
        logger.info(String.format("Authentication userId {%s} - updateNotification do userId {%s} e notificationId {%s}", userDetails.getUserId(), userId, notificationId));

        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return ResponseEntity.status(HttpStatus.OK).body(notificationService.updateNotification(notificationRecordDto, notificationService.findByNotificationIdAndUserId(notificationId, userId).get()));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
