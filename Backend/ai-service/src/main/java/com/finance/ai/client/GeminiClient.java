package com.finance.ai.client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GeminiClient {
    private final RestTemplate restTemplate;
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String apiBaseUrl;

    public GeminiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateTextWithSpring(String prompt) {
        String url = apiBaseUrl + "/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        
        String requestBody = buildRequestBody(prompt);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                String.class
            );
            
            return extractText(response.getBody());
        } catch (Exception e) {
            log.error("Error generating text with Gemini", e);
            throw new RuntimeException("Failed to generate text", e);
        }
    }
    
    private String buildRequestBody(String prompt) {
        return String.format(
            "{\"contents\": [{ \"parts\":[{\"text\": \"%s\"}] }] }", 
            prompt.replace("\"", "\\\"")
        );
    }

    public String extractText(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        
        JsonNode candidates = rootNode.path("candidates");
        if (candidates.isArray() && !candidates.isEmpty()) {
            JsonNode textNode = candidates.get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");
            
            String text = textNode.asText();
            return text.replaceAll("\\.\\s+", ". ");
        }
        
        throw new RuntimeException("No text found in Gemini API response");
    }
}