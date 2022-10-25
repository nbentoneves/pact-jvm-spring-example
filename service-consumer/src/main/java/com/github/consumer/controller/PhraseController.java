package com.github.consumer.controller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class PhraseController {

    private final RestTemplate restTemplate;

    public PhraseController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Getter
    @Builder
    private static class Response {
        private UUID id;
        private String phrase;
    }

    @Getter
    public static class ResponseRandomWord {
        private UUID id;
        private String word;
    }

    @GetMapping("/getPhrase")
    @ResponseBody
    public Response getPhrase() {

        ResponseRandomWord wordEntity = restTemplate.getForObject("/getOneRandomWord", ResponseRandomWord.class);

        return Response.builder()
                .id(UUID.randomUUID())
                .phrase("This is my phrase using " + Objects.requireNonNull(wordEntity).word + " word")
                .build();
    }

}
