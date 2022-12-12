package aic.g3t1.redisexample;

import aic.g3t1.common.environment.EnvironmentVariables;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] redisHost = EnvironmentVariables.getVariable("REDIS_HOST").split(":");
        String server = redisHost[0];
        int port = Integer.parseInt(redisHost[1]);

        System.out.println("Connecting to Redis through " + server + ":" + port);

        try (JedisPool pool = new JedisPool(server, port)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.set("clientName", "Jedis");
            }
        }

        System.out.println("Bye!");
    }
}