package com.ramv.storyverse.controller;

import com.ramv.storyverse.model.Scene;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/api/story")
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/generate-scenes")
    public List<Scene> generateScenes(
            @RequestParam(value = "story", defaultValue = "A brave bird") String storyPrompt
    ){
        // 1. Create the formatted prompt for the AI
        String aiPrompt = """
                You are a visual storyteller and director. 
                Analyze this story idea: "%s"
                
                Break it down into a short script with:
                - Scene Number
                - Visual Description (for an artist)
                - Camera Angle
                
                Keep it concise.
                """.formatted(storyPrompt);

        return chatClient.prompt()
                .user(aiPrompt)
                .call()
                .entity(new ParameterizedTypeReference<List<Scene>>() {});
    }

}
