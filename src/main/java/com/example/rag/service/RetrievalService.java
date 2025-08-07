package com.example.rag.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrievalService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public RetrievalService(EmbeddingModel embeddingModel, 
                          EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public List<String> retrieveRelevantChunks(String question, int topK) {
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        
        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(
            EmbeddingSearchRequest.builder()
                .queryEmbedding(questionEmbedding)
                .maxResults(topK)
                .build());
        
        return result.matches().stream()
            .map(EmbeddingMatch::embedded)
            .map(TextSegment::text)
            .toList();
    }
}