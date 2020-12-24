package com.takeaway.got.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface IntegrationGateway {

    @Gateway(requestChannel = "mqttMessageTransformerChannel")
    void sendToMqtt(Message<?> data);

}
