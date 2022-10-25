package com.github.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslResponse;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.github.consumer.controller.PhraseController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//https://docs.pact.io/implementation_guides/jvm/consumer/junit5#2-create-a-method-annotated-with-pact-that-returns-the-interactions-for-the-test
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "random-word")
class PhraseControllerTest {

    private final String randomWordResponse = """
                {
                  "id": "ff5ded7e-e030-47a6-a7f4-f8f92df323b6",
                  "word": "hello"
                }
            """.trim();

    @Pact(provider = "random-word", consumer = "phrase-consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("Word to retrieve")
                .uponReceiving("get a random word")
                .path("/getOneRandomWord")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .uuid("id")
                        .stringType("word")
                        .close())
                .toPact();
    }

    private AutoCloseable closeable;

    private RestTemplate restTemplate;

    @InjectMocks
    private PhraseController phraseController;

    @BeforeEach
    void setUp(MockServer mockServer) {
        closeable = MockitoAnnotations.openMocks(this);
        restTemplate = Mockito.spy(new RestTemplate());
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(mockServer.getUrl()));
        phraseController = new PhraseController(restTemplate);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void verifyCallToRandomWord() {
        phraseController.getPhrase();

        verify(restTemplate, times(1))
                .getForObject("/getOneRandomWord", PhraseController.ResponseRandomWord.class);
    }
}
