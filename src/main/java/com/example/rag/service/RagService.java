package com.example.rag.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@Service
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final MilvusEmbeddingStore milvusStore;
    private final ChatLanguageModel chatModel;
    private Assistant assistant;

    // Inject configuration values from application.properties
    public RagService(
            // Milvus values
            @Value("${milvus.host}") String milvusHost,
            @Value("${milvus.port}") int milvusPort,
            // Ollama values
            @Value("${ollama.host}") String ollamaHost,
            @Value("${ollama.port}") int ollamaPort
    ) {
        // Set up the EmbeddingModel for Ollama using the correct Docker service name
        this.embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://" + ollamaHost + ":" + ollamaPort)
                .modelName("all-minilm")
                .build();
        
        // Set up the ChatModel for Ollama using the correct Docker service name
        this.chatModel = OllamaChatModel.builder()
                .baseUrl("http://" + ollamaHost + ":" + ollamaPort)
                .modelName("tinyllama")
                .timeout(Duration.ofMinutes(5))
                .build();
        
        // Set up Milvus Embedding Store using the correct Docker service name
        this.milvusStore = MilvusEmbeddingStore.builder()
                .host(milvusHost)
                .port(milvusPort)
                .collectionName("football_docs") 
                .dimension(384) 
                .build();
    }

    @PostConstruct
    private void initialize() {
        System.out.println("==================================================");
        System.out.println("Initializing RAG Pipeline...");

        // 1. Build the Retriever that searches in Milvus
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(this.milvusStore)
                .embeddingModel(this.embeddingModel)
                .maxResults(3)
                .minScore(0.6)
                .build();
        System.out.println("[SUCCESS] Content Retriever is configured to search in Milvus.");

        // 2. Build the AI Assistant that connects everything
        this.assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(this.chatModel)
                .contentRetriever(contentRetriever)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
        System.out.println("[SUCCESS] AI Assistant is ready.");

        // 3. Ingest the data at startup
        ingestData();
        System.out.println("RAG Pipeline Initialized. The application is ready to accept questions.");
        System.out.println("==================================================");
    }

    private void ingestData() {
        System.out.println("- Starting data ingestion...");
        DocumentSplitter splitter = DocumentSplitters.recursive(400, 40);
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingStore(this.milvusStore)
                .embeddingModel(this.embeddingModel)
                .build();
        try {
            Path filePath = Paths.get("src/main/resources/data.txt");
            Document document = FileSystemDocumentLoader.loadDocument(filePath, new TextDocumentParser());
            ingestor.ingest(document);
            System.out.println("- Data ingestion complete.");
        } catch (Exception e) {
            System.err.println("- Could not ingest data: " + e.getMessage());
        }
    }

    // Method that will be called by the Controller
    public String ask(String question) {
        return assistant.chat(question);
    }

    // Assistant interface
    interface Assistant {
        String chat(String userMessage);
    }
}
