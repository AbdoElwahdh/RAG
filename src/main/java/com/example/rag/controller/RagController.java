package com.example.rag.controller;

import com.example.rag.service.RagService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final ChatLanguageModel chatModel;

    public RagController(RagService ragService) {
        this.ragService = ragService;
        this.chatModel = ragService.chatModel;
    }

    @PostMapping("/ingest")
    public String ingestData() {
        ragService.ingestAndChunkData();
        return "Data ingestion completed successfully!";
    }

    @GetMapping("/ask")
    public String askQuestion(@RequestParam String question) {
        return chatModel.generate(question);
    }
}