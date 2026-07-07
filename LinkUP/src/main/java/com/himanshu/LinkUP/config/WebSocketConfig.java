package com.himanshu.LinkUP.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig  implements WebSocketMessageBrokerConfigurer {
    private final WebSocketAuthChannelInterceptor interceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // web socket endpoint
                .setAllowedOriginPatterns("*") // allow any frontend to connect
                .withSockJS(); // fallback
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptor);
    }
}
/*
Tells spring how should i configure the webSocket
- Which endPoint to expose
- which protocol to use
- where message should go

implements WebSocketMessageBrokerConfigurer
- if you want to customise the webSocket implements this interface
It gives us 2 methods
  - registerStompEndpoints()
  - configureMessageBroker()


@EnableWebSocketMessageBroker -> without thiS websocket will be disabled


/topic/** → destinations for publish/subscribe (broadcast)
/queue/** → destinations for point-to-point (typically one user)
 */