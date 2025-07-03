package com.ikeda.notification.dtos;

import com.ikeda.notification.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public record NotificationRecordDto(@NotNull
                                    NotificationStatus notificationStatus) {
}
