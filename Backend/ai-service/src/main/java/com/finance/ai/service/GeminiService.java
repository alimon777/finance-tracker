package com.finance.ai.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.ai.client.ExpenditureServiceClient;
import com.finance.ai.client.GeminiClient;
import com.finance.ai.client.GoalServiceClient;
import com.finance.ai.dto.AiSuggestion;
import com.finance.ai.dto.Budget;
import com.finance.ai.dto.ExpenditureSummaryDTO;
import com.finance.ai.dto.Goal;

@Service
public class GeminiService {

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private ExpenditureServiceClient expenditureServiceClient;

    @Autowired
    private GoalServiceClient goalServiceClient;

    public String generateText(String prompt) {
        return geminiClient.generateTextWithSpring(prompt);
    }

    public String generatePrompt(Long userId) {
        // Fetch expenditure summary
        ExpenditureSummaryDTO expenditureSummary = expenditureServiceClient.getExpenditureSummary(userId).getBody();
        
        // Fetch user goals
        List<Goal> goals = goalServiceClient.getAllGoals(userId).getBody();
        
        // Build a comprehensive prompt for budget and goal analysis
        StringBuilder promptBuilder = new StringBuilder();
        
        // Add expenditure information
        if (expenditureSummary != null) {
            promptBuilder.append("Expenditure Summary:\n");
            
            // Weekly expenditure details
            if (expenditureSummary.getWeekly() != null) {
                promptBuilder.append("Weekly Withdrawals: ").append(expenditureSummary.getWeekly().getWithdrawTotal()).append("\n");
                promptBuilder.append("Weekly Withdrawal Categories: ").append(formatCategories(expenditureSummary.getWeekly().getWithdraw())).append("\n");
            }
            
            // Monthly expenditure details
            if (expenditureSummary.getMonthly() != null) {
                promptBuilder.append("Monthly Withdrawals: ").append(expenditureSummary.getMonthly().getWithdrawTotal()).append("\n");
                promptBuilder.append("Monthly Withdrawal Categories: ").append(formatCategories(expenditureSummary.getMonthly().getWithdraw())).append("\n");
            }
            
            // Yearly expenditure details
            if (expenditureSummary.getYearly() != null) {
                promptBuilder.append("Yearly Withdrawals: ").append(expenditureSummary.getYearly().getWithdrawTotal()).append("\n");
                promptBuilder.append("Yearly Withdrawal Categories: ").append(formatCategories(expenditureSummary.getYearly().getWithdraw())).append("\n");
            }
        }
        
        // Add goals information
        if (goals != null && !goals.isEmpty()) {
            promptBuilder.append("\nUser Goals:\n");
            goals.forEach(goal -> {
                promptBuilder.append("- ").append(goal.getGoalName())
                             .append(": ").append(goal.getValue())
                             .append(" (Duration: ").append(goal.getDurationInMonths()).append(" months)\n");
            });
        }
        
        promptBuilder.append("\nProvide a detailed budget suggestion and strategy to help the user manage their finances and achieve their goals.");
        
        return promptBuilder.toString();
    }

    public AiSuggestion processResponse(String response) {
        Budget aiBudget = new Budget();
        aiBudget.setAiGenerated(true);
        
        // Parse the response and populate budget categories
        String[] categories = {"food", "housing", "transportation", "entertainment"};
        double total = 0;
        
        for (String category : categories) {
            // Simple parsing logic - you might want to enhance this with more robust parsing
            double amount = extractAmountForCategory(response, category);
            switch (category) {
                case "food":
                    aiBudget.setFood(amount);
                    break;
                case "housing":
                    aiBudget.setHousing(amount);
                    break;
                case "transportation":
                    aiBudget.setTransportation(amount);
                    break;
                case "entertainment":
                    aiBudget.setEntertainment(amount);
                    break;
            }
            total += amount;
        }
        
        aiBudget.setTotal(total);
        
        // Create AI suggestion with text content and budget
        return new AiSuggestion(response, aiBudget);
    }
    
    // Helper method to format withdrawal categories
    private String formatCategories(java.util.Map<String, Double> categories) {
        if (categories == null || categories.isEmpty()) return "No categories";
        
        return categories.entrySet().stream()
            .map(entry -> entry.getKey() + ": $" + String.format("%.2f", entry.getValue()))
            .collect(Collectors.joining(", "));
    }
    
    // Helper method to extract budget amount for a category (very basic implementation)
    private double extractAmountForCategory(String response, String category) {
        // This is a very simplistic parsing method
        // In a real-world scenario, you'd want more sophisticated NLP or regex parsing
        try {
            String[] words = response.toLowerCase().split("\\s+");
            for (int i = 0; i < words.length; i++) {
                if (words[i].contains(category)) {
                    // Look for a number after the category
                    if (i + 1 < words.length) {
                        String potentialAmount = words[i + 1].replaceAll("[^0-9.]", "");
                        return Double.parseDouble(potentialAmount);
                    }
                }
            }
        } catch (Exception e) {
            // Log error or handle parsing failure
        }
        return 0.0; // Default to 0 if no amount found
    }
}