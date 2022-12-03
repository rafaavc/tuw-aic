package aic.g3t1.consumer.redis;

import redis.clients.jedis.JedisCommands;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class JedisCommandsCloseable implements AutoCloseable {

    private final Supplier<JedisCommands> getInstanceFn;
    private final Consumer<JedisCommands> returnInstanceFn;
    private JedisCommands instance;

    public JedisCommandsCloseable(Supplier<JedisCommands> getInstanceFn, Consumer<JedisCommands> returnInstanceFn) {
        this.getInstanceFn = getInstanceFn;
        this.returnInstanceFn = returnInstanceFn;
    }

    public JedisCommands open() {
        if (instance == null) {
            instance = getInstanceFn.get();
        }
        return instance;
    }

    @Override
    public void close() {
        if (instance != null) {
            returnInstanceFn.accept(instance);
            instance = null;
        }
    }

}
