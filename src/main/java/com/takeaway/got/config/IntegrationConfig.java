package com.takeaway.got.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.got.dto.CurrentPlayedDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;

public class IntegrationConfig {

    @Configuration
    public class IntegrationConfig {

        @Bean
        @Transformer(inputChannel = "mqttHeaderEnricherChannel", outputChannel = "mqttOutboundChannel")
        public ObjectToJsonTransformer enrichHeader() {
            return new ObjectToJsonTransformer(getMapper());

        }

        @Bean
        public Jackson2JsonObjectMapper getMapper() {
            return new Jackson2JsonObjectMapper(new ObjectMapper());

        }

        @Bean
        @Transformer(inputChannel = "mqttInputChannel", outputChannel = "inputAfterTransform")
        JsonToObjectTransformer jsonToObjectTransformer() {
            return new JsonToObjectTransformer(CurrentPlayedDto.class);
        }
}
