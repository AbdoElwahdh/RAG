package com.example.rag.retriever;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.retriever.Retriever;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MilvusRetriever {

    private final MilvusEmbeddingStore milvusStore;
    private final EmbeddingModel embeddingModel;

    public MilvusRetriever(MilvusEmbeddingStore milvusStore, EmbeddingModel embeddingModel) {
        this.milvusStore = milvusStore;
        this.embeddingModel = embeddingModel;
    }

    public List<TextSegment> retrieveRelevantChunks(String userQuestion) {
        Embedding embedding = embeddingModel.embed(userQuestion).content();
        return milvusStore.findRelevant(embedding, 5).stream()
                .map(match -> match.embedded())
                .toList();
    }

    // المطلوبة من RagController
    public Retriever<TextSegment> getRetriever() {
        return EmbeddingStoreRetriever.from(milvusStore, embeddingModel);
    }
}
