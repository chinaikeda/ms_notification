package com.ikeda.notification.services;

import com.ikeda.notification.dtos.NotificationRecordCommandDto;
import com.ikeda.notification.dtos.NotificationRecordDto;
import com.ikeda.notification.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface NotificationService {
    NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto);

    Page<NotificationModel> findAllNotificationsByUser(UUID userId, Pageable pageable);

    Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId);

    NotificationModel updateNotification(NotificationRecordDto notificationRecordDto, NotificationModel notificationModel);
}
