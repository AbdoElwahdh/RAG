package com.example.rag.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RagService {


    public OllamaEmbeddingModel embeddingModel;
    public ChatLanguageModel chatModel;

    @PostConstruct
    public void initializeModels() {
        System.out.println("==================================================");
        System.out.println("Initializing Ollama Models for the RAG application...");

        try {
            // 1. إعداد نموذج التضمين (all-minilm)
            this.embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl("http://localhost:11434" )
                    .modelName("all-minilm")
                    .timeout(Duration.ofSeconds(60))
                    .build();
            System.out.println("[SUCCESS] Embedding Model (all-minilm) is ready.");

            // 2. إعداد نموذج المحادثة (phi3)
            this.chatModel = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434" )
                    .modelName("phi3")
                    .timeout(Duration.ofMinutes(5))
                    .build();
            System.out.println("[SUCCESS] Chat Model (phi3) is ready.");

        } catch (Exception e) {
            System.err.println("[FAILURE] A model failed to initialize. Please check that Ollama is running.");
            e.printStackTrace();
        }

        System.out.println("Model initialization complete.");
        System.out.println("==================================================");
    }


}
