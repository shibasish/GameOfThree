package com.takeaway.got.service;

import com.takeaway.got.dto.CurrentPlayedDto;
import com.takeaway.got.gateway.IntegrationGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    IntegrationGateway integrationGateway;

    @Autowired
    MqttPahoMessageHandler mqttOutbound;

    @Value("${gameofthree.fromPlayerId}")
    private String fromPlayer;

    public String startGame(CurrentPlayedDto currentPlayedDto) {

        UUID uuid = UUID.randomUUID();
        int startingNumber = new Random().nextInt(10000);

        currentPlayedDto.setNumber(startingNumber);
        Message<CurrentPlayedDto> message = new GenericMessage<CurrentPlayedDto>(currentPlayedDto);
        this.integrationGateway.sendToMqtt(message);

        return "Played Successfully";
    }

    public String playTurn(CurrentPlayedDto currentPlayedDto) {

        if(currentPlayedDto.getNumber() == 0) {
            return "You Lost!!";
        }

        int val = calculateNextMove(currentPlayedDto.getNumber());

        if(val == 1) {
            CurrentPlayedDto currentPlayedDto2 = new CurrentPlayedDto();
            currentPlayedDto2.setNumber(0);
            this.integrationGateway.sendToMqtt(new GenericMessage<CurrentPlayedDto>(currentPlayedDto2));
            return "You Won!!";
        }

        CurrentPlayedDto currentPlayedDto3 = new CurrentPlayedDto();
        currentPlayedDto3.setFromPlayer(fromPlayer);
        currentPlayedDto3.setToPlayer(currentPlayedDto.getToPlayer());
        currentPlayedDto3.setNumber(val);

        Message<CurrentPlayedDto> message = new GenericMessage<CurrentPlayedDto>(currentPlayedDto3);
        System.out.println("sending to p2: "+ val);

        this.integrationGateway.sendToMqtt(message);
        return "Successfully played!";
    }


    private int calculateNextMove(int playedNumber) {

        if(playedNumber%3 == 0) {
            int nextVal = playedNumber/3;
            if(nextVal == 1) {
                return 1;
            }
            return nextVal;
        }
        else if(playedNumber%3 == 1) {
            int nextVal = (playedNumber-1)/3;
            if(nextVal == 1) {
                return 1;
            }
            return nextVal;
        }
        else if(playedNumber%3 == 2) {
            int nextVal = (playedNumber+1)/3;
            if(nextVal == 1) {
                return 1;
            }
            return nextVal;
        }

        return 0;
    }

}
