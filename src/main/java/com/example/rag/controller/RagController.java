package com.example.rag.controller;

import com.example.rag.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RagController {

    private final RagService ragService;

    
    @Autowired
    public RagController(RagService ragService ) {
        this.ragService = ragService;
    }

    // This is the endpoint we will use in the browser
    // Example: http://localhost:8080/ask?question=Who is Lionel Messi
    @GetMapping("/ask" )
    public ResponseEntity<Map<String, String>> askQuestion(@RequestParam String question) {
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question parameter cannot be empty."));
        }

        System.out.println("\nReceived a new question via API: '" + question + "'");
        // Call the service that contains the RAG logic
        String answer = ragService.ask(question);

        // Return the answer as JSON
        return ResponseEntity.ok(Map.of(
                "question", question,
                "answer", answer
        ));
    }
}
