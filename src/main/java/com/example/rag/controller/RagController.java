package com.example.rag.controller;

import com.example.rag.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RagController {

    private final RagService ragService;

    // Spring سيقوم بحقن RagService هنا تلقائيًا
    @Autowired
    public RagController(RagService ragService ) {
        this.ragService = ragService;
    }

    // هذا هو الرابط الذي سنستخدمه في المتصفح
    // مثال: http://localhost:8080/ask?question=Who is Lionel Messi
    @GetMapping("/ask" )
    public ResponseEntity<Map<String, String>> askQuestion(@RequestParam String question) {
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question parameter cannot be empty."));
        }

        System.out.println("\nReceived a new question via API: '" + question + "'");
        // استدعاء الخدمة التي تحتوي على منطق RAG
        String answer = ragService.ask(question);

        // إرجاع الإجابة في صيغة JSON
        return ResponseEntity.ok(Map.of(
                "question", question,
                "answer", answer
        ));
    }
}
