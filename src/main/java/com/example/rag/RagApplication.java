package com.example.rag;

import com.example.rag.service.RagService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RagApplication {

    private final RagService ragService;

    public RagApplication(RagService ragService) {
        this.ragService = ragService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RagApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        ragService.ingestAndChunkData();
    }
}