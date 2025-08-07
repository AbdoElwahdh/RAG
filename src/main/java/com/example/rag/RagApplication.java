package com.example.rag;

import com.example.rag.service.RagService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class RagApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagApplication.class, args);
    }

    @Autowired
    private RagService ragService;

    @PostConstruct
    public void init() {
        ragService.ingestAndChunkData();
    }
}
