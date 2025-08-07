package com.example.rag.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuiController {

    private final RagController ragController;

    public GuiController(RagController ragController) {
        this.ragController = ragController;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("question", "");
        model.addAttribute("answer", "");
        model.addAttribute("topK", 3);
        return "index";
    }

    @PostMapping("/ask")
    public String askQuestion(
            @RequestParam String question,
            @RequestParam(defaultValue = "3") int topK,
            Model model) {
        
        String answer = ragController.askQuestion(question, topK);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        model.addAttribute("topK", topK);
        return "index";
    }
}