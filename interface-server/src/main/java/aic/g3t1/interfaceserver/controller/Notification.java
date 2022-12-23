package aic.g3t1.interfaceserver.controller;

import aic.g3t1.interfaceserver.model.TaxiNotification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class Notification {
    @PostMapping
    public String hello(@RequestBody TaxiNotification notification) {
        return notification.toString();
    }
}
