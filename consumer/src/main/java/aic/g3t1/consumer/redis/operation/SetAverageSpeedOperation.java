package aic.g3t1.consumer.redis.operation;

import aic.g3t1.common.redis.RedisHashes;
import redis.clients.jedis.JedisCommands;

public class SetAverageSpeedOperation implements RedisOperation {
    private static final long serialVersionUID = -3765142409606401507L;

    private final int taxiNumber;
    private final double avgSpeed;

    public SetAverageSpeedOperation(int taxiNumber, double avgSpeed) {
        this.taxiNumber = taxiNumber;
        this.avgSpeed = avgSpeed;
    }

    @Override
    public void perform(JedisCommands commands) {
        commands.hset(RedisHashes.R_AVERAGE_SPEED_HASH, String.valueOf(taxiNumber), String.valueOf(avgSpeed));
    }

    @Override
    public String toString() {
        return "{ " +
                "\"taxiNumber\": " + taxiNumber +
                ", \"averageSpeed\": " + avgSpeed +
                " }";
    }
}
