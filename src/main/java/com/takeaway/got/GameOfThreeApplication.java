package com.takeaway.got;

import java.util.Optional;

import com.takeaway.got.model.Player;
import com.takeaway.got.service.PlayerService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

import com.takeaway.got.model.GAMEMODE;
import com.takeaway.got.repo.PlayerRepo;


@SpringBootApplication
@Configuration
public class GameOfThreeApplication implements CommandLineRunner {

	@Value("${gameofthree.fromPlayerId}")
	private String fromPlayer;

	@Value("${mqtt.borker.url}")
	private String brokerUrl;

	@Value("${mqtt.borker.username}")
	private String userName;

	@Value("${mqtt.borker.password}")
	private String password;
	
	@Value("${gameofthree.game.mode}")
	private GAMEMODE gameMode;
	
	@Autowired
	PlayerRepo playerRepo;

	@Autowired
	PlayerService playerService;

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MqttPahoMessageDrivenChannelAdapter inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(brokerUrl, fromPlayer+"Subscriber", "from/+/to/"+fromPlayer);

		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		adapter.setManualAcks(true);
		return adapter;
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {

		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { brokerUrl });
		options.setUserName(userName);
		options.setPassword(password.toCharArray());
		options.setCleanSession(false);
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	public static void main(String[] args) {
		SpringApplication.run(GameOfThreeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Optional<Player> player = playerRepo.findById(fromPlayer);
		player.orElse(createPlayer());

	}
	
	private Player createPlayer() {
		playerService.createPlayer();
		return null;
	}
}
