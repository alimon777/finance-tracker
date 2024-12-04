package com.finance.ai.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @PostMapping("/suggestion")
    public ResponseEntity<String> generateSuggestion(@RequestBody Long userId) {
    	String prompt = geminiService.generatePrompt(userId);
        String response = geminiService.generateText(prompt);
        return ResponseEntity.ok(response);
    }
}