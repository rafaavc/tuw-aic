package aic.g3t1.interfaceserver.controller.websocket;

import aic.g3t1.interfaceserver.controller.cors.CORSFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    public final static String wsEndpoint = "/ws";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(wsEndpoint).setAllowedOrigins(CORSFilter.corsOrigin).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker(Topic.prefix);
        // this can be used if we need to receive ws data here in the server (I believe):
        //config.setApplicationDestinationPrefixes("/app");
    }
}
