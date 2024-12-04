package com.finance.ai.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.ai.dto.AiSuggestion;
import com.finance.ai.service.GeminiService;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {
	
	@Autowired
    private GeminiService geminiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateText(@RequestBody String prompt) {
        String response = geminiService.generateText(prompt);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/suggestion/{userId}")
    public ResponseEntity<AiSuggestion> generateSuggestion(@PathVariable Long userId) {
    	String prompt = geminiService.generatePrompt(userId);
        String response = geminiService.generateText(prompt);
        AiSuggestion processedResponse = geminiService.processResponse(response);
        return ResponseEntity.ok(processedResponse);
    }
}