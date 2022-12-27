package aic.g3t1.interfaceserver.controller;

import aic.g3t1.common.environment.EnvironmentVariables;
import aic.g3t1.common.exceptions.MissingEnvironmentVariableException;
import aic.g3t1.common.redis.RedisHashes;
import aic.g3t1.interfaceserver.controller.websocket.Topic;
import aic.g3t1.interfaceserver.model.TaxiData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
public class TaxiDataController {
    @Autowired
    private SimpMessagingTemplate template;
    private final Jedis jedis;

    private class RedisPoller implements Runnable {
        @Override
        public void run() {
            if (template == null) return;

            System.out.println("-> POLLING REDIS :)");

            var data = new TaxiData.Builder()
                    .distances(jedis.hgetAll(RedisHashes.R_DISTANCE_HASH))
                    .averageSpeeds(jedis.hgetAll(RedisHashes.R_AVERAGE_SPEED_HASH))
                    .locations(jedis.hgetAll(RedisHashes.R_LOCATION_HASH))
                    .build();

            template.convertAndSend(Topic.taxis, data);

        }
    }

    public TaxiDataController() throws MissingEnvironmentVariableException {
        String[] redisHost = EnvironmentVariables.getVariable("REDIS_HOST").split(":");
        String server = redisHost[0];
        int port = Integer.parseInt(redisHost[1]);

        System.out.println("Connecting to Redis through " + server + ":" + port);

        JedisPool pool = new JedisPool(server, port);
        jedis = pool.getResource();

        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(new RedisPoller(), 0L, 5L, TimeUnit.SECONDS);
    }
}
