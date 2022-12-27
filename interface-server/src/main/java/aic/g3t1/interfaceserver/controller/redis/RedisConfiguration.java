package aic.g3t1.interfaceserver.controller.redis;

import aic.g3t1.common.environment.EnvironmentVariables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    @Bean
    JedisConnectionFactory jedisConnectionFactory() throws Exception {
        String[] redisHost = EnvironmentVariables.getVariable("REDIS_HOST").split(":");
        String hostName = redisHost[0];
        int port = Integer.parseInt(redisHost[1]);

        return new JedisConnectionFactory(new RedisStandaloneConfiguration(hostName, port));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws Exception {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
