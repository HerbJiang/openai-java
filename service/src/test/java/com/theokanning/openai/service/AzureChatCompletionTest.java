package com.theokanning.openai.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.theokanning.openai.chat.ChatCompletionChoice;
import com.theokanning.openai.chat.ChatCompletionRequest;
import com.theokanning.openai.chat.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class AzureChatCompletionTest {

    String token = System.getenv("OPENAI_APIKEY");
    AzureOpenAiService service = new AzureOpenAiService("2261e21dace94b31b25d83a3c394b0d2", "intumit-project-sit", "gpt-4");

    @Test
    void createCompletion() {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(Collections.singletonList(new Message("user", "你好")))
                .user("testing")
                .logitBias(new HashMap<>())
                .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();

        System.out.println(choices);
        assertEquals(1, choices.size());
        assertEquals("assistant", choices.get(0).getMessage().getRole());
    }

}
