package com.github.producer.controller;

import com.github.producer.repository.WordsInMemoryRepository;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class RandomWordController {

    @Getter
    @Builder
    private static class Response {
        private UUID id;
        private String word;
    }

    private final Random random = new Random();

    private final Set<String> listOfWords = Set.of("hello", "city", "Portugal");

    private final WordsInMemoryRepository wordsRepository;

    public RandomWordController(WordsInMemoryRepository wordsRepository) {
        requireNonNull(wordsRepository, "wordsRepository can not be null");
        this.wordsRepository = wordsRepository;
    }

    @GetMapping("/getOneRandomWord")
    @ResponseBody
    public Response getOneRandomWord() {
        return Response.builder()
                .word(listOfWords.stream().skip(random.nextInt(listOfWords.size())).findFirst().orElse("outside"))
                .id(UUID.randomUUID())
                .build();
    }
}
