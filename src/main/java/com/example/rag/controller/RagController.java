package com.example.rag.controller;

import com.example.rag.service.RagService;
import com.example.rag.service.RetrievalService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final RetrievalService retrievalService;
    private final ChatLanguageModel chatModel;

    public RagController(RagService ragService, 
                       RetrievalService retrievalService) {
        this.ragService = ragService;
        this.retrievalService = retrievalService;
        this.chatModel = ragService.chatModel; // Access chatModel from RagService
    }

    @PostMapping("/ingest")
    public String ingestData() {
        ragService.ingestAndChunkData();
        return "Data ingestion completed successfully!";
    }

    @GetMapping("/ask")
    public String askQuestion(@RequestParam String question,
                            @RequestParam(defaultValue = "3") int topK) {
        // Retrieve relevant chunks
        List<String> relevantChunks = retrievalService.retrieveRelevantChunks(question, topK);
        
        // Combine with original question
        String augmentedPrompt = String.format(
            "Answer based on these contexts:\n%s\n\nQuestion: %s", 
            String.join("\n\n", relevantChunks),
            question
        );
        
        // Generate response using LLM
        return chatModel.generate(augmentedPrompt);
    }
}