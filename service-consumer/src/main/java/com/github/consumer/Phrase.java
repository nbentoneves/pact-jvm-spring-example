package com.github.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootApplication
public class Phrase {

    @Bean
    public RestTemplate restTemplate(@Value("${client.url.randomWord}") String clientUrl) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(clientUrl));

        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Phrase.class, args);
    }

}
