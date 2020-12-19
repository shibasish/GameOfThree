package com.takeaway.got.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface IntegrationGateway {


    @Gateway(requestChannel = "mqttHeaderEnricherChannel")
    void sendToMqtt(Message<?> data);

    @Gateway(requestChannel = "mqttSendSuccessChannel")
    void sendSuccess(Message<?> data);
}
