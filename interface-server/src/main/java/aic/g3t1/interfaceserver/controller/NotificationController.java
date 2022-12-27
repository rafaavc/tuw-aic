package aic.g3t1.interfaceserver.controller;

import aic.g3t1.common.model.notification.TaxiNotification;
import aic.g3t1.interfaceserver.controller.websocket.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping
    public void notify(@RequestBody TaxiNotification notification) {
        this.template.convertAndSend(Topic.notifications, notification);
    }
}
