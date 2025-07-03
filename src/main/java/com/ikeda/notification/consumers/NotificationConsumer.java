package com.ikeda.notification.consumers;

import com.ikeda.notification.dtos.NotificationRecordCommandDto;
import com.ikeda.notification.services.NotificationService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ikeda.broker.queue.notificationCommandQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ikeda.broker.exchange.notificationCommandExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "${ikeda.broker.key.notificationCommandKey}")
    )
    public void listen(@Payload NotificationRecordCommandDto notificationRecordCommandDto){
        notificationService.saveNotification(notificationRecordCommandDto);
    }
}
