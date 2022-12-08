package aic.g3t1.consumer.redis.operation;

import redis.clients.jedis.JedisCommands;

import java.io.Serializable;

public interface RedisOperation extends Serializable {

    String F_GROUP = "group";
    String F_REDIS_OPERATION = "redis_op";

    void perform(JedisCommands commands);

}
