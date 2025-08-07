package com.example.rag.controller;

import com.example.rag.retriever.MilvusRetriever;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.retriever.Retriever;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final ConversationalRetrievalChain chain;

    @Autowired
    public RagController(MilvusRetriever milvusRetriever,
                         ChatLanguageModel chatModel) {

        Retriever<TextSegment> retriever = milvusRetriever.getRetriever();

        this.chain = ConversationalRetrievalChain.builder()
                .chatLanguageModel(chatModel)
                .retriever(retriever)
                .build();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return chain.execute(question);
    }
}
