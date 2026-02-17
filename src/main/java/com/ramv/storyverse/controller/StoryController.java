package com.ramv.storyverse.controller;

import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoryController {

    private final ChatClient chatClient;

    public StoryController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    @GetMapping("/hello")
    public String testAi(){
        return chatClient.prompt()
                .user("Tell me one sentence story about a brave bird escaping fire")
                .call()
                .content();
    }

    @GetMapping("/models")
    public String listModels() {
        // This is a "hack" to force the error to tell us the valid models
        // We intentionally ask for a fake model, and the error usually lists the valid ones.
        // Or, if your library supports it, we just return a message to check logs.

        // For now, let's try the ONE model that almost always works on v1beta:
        return "Check your console logs for the error message!";
    }

}
