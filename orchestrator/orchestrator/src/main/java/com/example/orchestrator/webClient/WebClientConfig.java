package com.example.orchestrator.webClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
        return webClient;
    }

}
