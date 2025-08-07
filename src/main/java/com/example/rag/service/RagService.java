package com.example.rag.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final MilvusEmbeddingStore milvusStore;
    private final ChatLanguageModel chatModel;

    @Autowired
    public RagService(EmbeddingModel embeddingModel,
                      MilvusEmbeddingStore milvusStore,
                      ChatLanguageModel chatModel) {

        this.embeddingModel = embeddingModel;
        this.milvusStore = milvusStore;
        this.chatModel = chatModel;

        System.out.println("Step 1: Models and Milvus injected successfully.");
    }

    public void ingestAndChunkData() {
        System.out.println("\nStep 2: Starting data ingestion and chunking process...");

        DocumentSplitter splitter = DocumentSplitters.recursive(400, 40);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingStore(this.milvusStore)
                .embeddingModel(this.embeddingModel)
                .build();

        try {
            Path filePath = Paths.get("src/main/resources/data.txt");
            Document document = FileSystemDocumentLoader.loadDocument(filePath, new TextDocumentParser());
            System.out.println("  - Reading document from data.txt...");

            List<String> chunks = splitter.split(document).stream()
                    .map(segment -> segment.text())
                    .toList();
            System.out.println("  - Document has been split into " + chunks.size() + " chunks.");

            ingestor.ingest(document);

            System.out.println("\n==================================================");
            System.out.println("  TASK COMPLETED SUCCESSFULLY!");
            System.out.println("  The document has been split, and all " + chunks.size() + " chunks are now stored in Milvus.");
            System.out.println("  Both Embedding and Chat models are initialized and ready for the next developer.");
            System.out.println("==================================================");

        } catch (Exception e) {
            throw new RuntimeException("FATAL: Failed to ingest document.", e);
        }
    }
}
