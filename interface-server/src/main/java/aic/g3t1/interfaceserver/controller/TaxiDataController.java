package aic.g3t1.interfaceserver.controller;

import aic.g3t1.interfaceserver.controller.websocket.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class TaxiDataController {
    @Autowired
    private SimpMessagingTemplate template;

    private class RedisPoller implements Runnable {
        @Override
        public void run() {
            if (template == null) return;
            System.out.println("----> POLLING REDIS!! :-)" + template);
            template.convertAndSend(Topic.taxis, "{\"prop\": \"test\"}");
        }
    }

    private ScheduledExecutorService executor;

    public TaxiDataController() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(new RedisPoller(), 0L, 5L, TimeUnit.SECONDS);
    }
}
