package com.takeaway.got.listener;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.gateway.IntegrationGateway;
import com.takeaway.got.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;


@Component
@Configuration
public class MessageListener {

    @Autowired
    MqttPahoClientFactory mqttPahoClientFactory;

    @Autowired
    GameService gameService;

    @Autowired
    IntegrationGateway integrationGateway;

    @Value("${gameofthree.fromPlayerId}")
    private String fromPlayer;

    @Value("${gameofthree.toPlayerId}")
    private String toPlayer;

    @Value("${mqtt.borker.url}")
    private String brokerUrl;

    @Value("${mqtt.borker.username}")
    private String userName;

    @Value("${mqtt.borker.password}")
    private String password;


    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel", async = "true")
    public MqttPahoMessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(fromPlayer + "Publisher", mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("from/" + fromPlayer + "/to/" + toPlayer);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultRetained(true);
        return messageHandler;
    }

    @Bean
    @ServiceActivator(inputChannel = "inputAfterTransform", async = "true")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("reveived from P2: " + message.getPayload());
                if (message.getPayload() instanceof CurrentPlayedDto) {

                    CurrentPlayedDto currentPlayedDto = (CurrentPlayedDto) message.getPayload();
//                     if(currentPlayedDto.getNumber() >= 2)
                    gameService.playTurn(currentPlayedDto);

                }
            }

        };
    }
}
