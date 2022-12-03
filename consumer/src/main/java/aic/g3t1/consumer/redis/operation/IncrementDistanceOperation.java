package aic.g3t1.consumer.redis.operation;

import redis.clients.jedis.JedisCommands;

public class IncrementDistanceOperation implements RedisOperation {

    public static final String R_DISTANCE_HASH = "distance";
    private static final long serialVersionUID = -5613982850836152034L;

    private final Integer taxiNumber;
    private final Double distance;

    public IncrementDistanceOperation(int taxiNumber, double distance) {
        this.taxiNumber = taxiNumber;
        this.distance = distance;
    }

    @Override
    public void perform(JedisCommands commands) {
        commands.hincrByFloat(R_DISTANCE_HASH, String.valueOf(taxiNumber), distance);
    }

}
