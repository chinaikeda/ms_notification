package com.ikeda.notification.services.impl;

import com.ikeda.notification.dtos.NotificationRecordCommandDto;
import com.ikeda.notification.dtos.NotificationRecordDto;
import com.ikeda.notification.enums.NotificationStatus;
import com.ikeda.notification.exceptions.NotFoundException;
import com.ikeda.notification.models.NotificationModel;
import com.ikeda.notification.repositories.NotificationRepository;
import com.ikeda.notification.services.NotificationService;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto) {
        var notificationModel = new NotificationModel();
        BeanUtils.copyProperties(notificationRecordCommandDto, notificationModel);
        notificationModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationModel.setNotificationStatus(NotificationStatus.CREATED);

        return notificationRepository.save(notificationModel);
    }

    @Override
    public Page<NotificationModel> findAllNotificationsByUser(UUID userId, Pageable pageable) {
        return notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
    }

    @Override
    public Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        Optional<NotificationModel> notificationModelOptional = notificationRepository.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationModelOptional.isEmpty()){
            throw new NotFoundException("Error: Notification for this user not found.");
        }

        return notificationModelOptional;
    }

    @Override
    public NotificationModel updateNotification(NotificationRecordDto notificationRecordDto, NotificationModel notificationModel) {
        notificationModel.setNotificationStatus(notificationRecordDto.notificationStatus());

        return notificationRepository.save(notificationModel);
    }
}