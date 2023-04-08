package com.theokanning.openai.service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.theokanning.openai.generation.GenerationRequest;
import com.theokanning.openai.generation.GenerationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GenerationTest {

    String token = "sk-INTUMITINC";
    TwsAiService service = new TwsAiService(token, Duration.ofMinutes(1));

    @Test
    void createCompletion() throws JsonProcessingException {
        GenerationRequest completionRequest = GenerationRequest.builder()
                .text(Collections.singletonList("Hello"))
                .temperature(1d)
                .topK(50d)
                .topP(1d)
                .repetitionPenalty(1d)
                .doSample(true)
                .removeInputFromOutput(true)
                .maxNewTokens(2048)
                .build();

        System.out.println(TwsAiService.defaultObjectMapper().writeValueAsString(completionRequest));
        GenerationResult result = service.createGeneration(completionRequest);
        List<String> choices = result.getText();
        assertEquals(1, choices.size());
        System.out.println(choices.get(0));
    }

}
