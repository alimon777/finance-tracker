package com.finance.ai.service;

import java.time.LocalDate;
import java.util.Arrays;
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
        ExpenditureSummaryDTO expenditureSummary = expenditureServiceClient.getExpenditureSummary(userId).getBody();
        List<Goal> goals = goalServiceClient.getAllGoals(userId).getBody();
        StringBuilder promptBuilder = new StringBuilder();
        
        if (expenditureSummary != null) {
            promptBuilder.append("Expenditure Summary:\n");
            if (expenditureSummary.getWeekly() != null) {
                promptBuilder.append("Weekly Withdrawals: ").append(expenditureSummary.getWeekly().getWithdrawTotal()).append("\n");
                promptBuilder.append("Weekly Withdrawal Categories: ").append(formatCategories(expenditureSummary.getWeekly().getWithdraw())).append("\n");
            }
            if (expenditureSummary.getMonthly() != null) {
                promptBuilder.append("Monthly Withdrawals: ").append(expenditureSummary.getMonthly().getWithdrawTotal()).append("\n");
                promptBuilder.append("Monthly Withdrawal Categories: ").append(formatCategories(expenditureSummary.getMonthly().getWithdraw())).append("\n");
            }
            if (expenditureSummary.getYearly() != null) {
                promptBuilder.append("Yearly Withdrawals: ").append(expenditureSummary.getYearly().getWithdrawTotal()).append("\n");
                promptBuilder.append("Yearly Withdrawal Categories: ").append(formatCategories(expenditureSummary.getYearly().getWithdraw())).append("\n");
            }
        }
        if (goals != null && !goals.isEmpty()) {
            promptBuilder.append("\nUser Goals:\n");
            goals.forEach(goal -> {
                promptBuilder.append("- ").append(goal.getGoalName())
                             .append(": INR ").append(goal.getValue())
                             .append(" (Duration: ").append(goal.getDurationInMonths()).append(" months)\n");
            });
        }
        promptBuilder.append("\nPlease provide a detailed budget suggestion in the following EXACT format and remember the following points while generation 1. remember to change figures(are in rupees) and dates in each response 2. start date should be greater than today but less than end date 3. The figures and date range should be relative:\n\n");
        promptBuilder.append("BUDGET_SUGGESTION_START\n");
        promptBuilder.append("Start Date: [YYYY-MM-DD]\n");
        promptBuilder.append("End Date: [YYYY-MM-DD]\n");
        promptBuilder.append("Food: [FOOD_AMOUNT_IN_INR]\n");
        promptBuilder.append("Housing: [HOUSING_AMOUNT_IN_INR]\n");
        promptBuilder.append("Transportation: [TRANSPORTATION_AMOUNT_IN_INR]\n");
        promptBuilder.append("Entertainment: [ENTERTAINMENT_AMOUNT_IN_INR]\n");
        promptBuilder.append("BUDGET_SUGGESTION_END\n\n");
        promptBuilder.append("Additional Insights: [PROVIDE_BRIEF_FINANCIAL_ADVICE_AND_STRATEGY_UNDER_100_WORDS_IN_SINGLE_PARAGRAPH_IN_INDIAN_RUPEES]\n");
        
        return promptBuilder.toString();
    }

    public AiSuggestion processResponse(String response) {
        Budget aiBudget = new Budget();
        aiBudget.setAiGenerated(true);
        try {
            String budgetSection = response.split("BUDGET_SUGGESTION_START")[1].split("BUDGET_SUGGESTION_END")[0];
            
            // Extract start and end dates
            LocalDate startDate = extractDate(budgetSection, "Start Date:");
            LocalDate endDate = extractDate(budgetSection, "End Date:");
            // Set dates to the budget object (assuming Budget class has these fields)
            aiBudget.setBudgetStartDate(startDate);
            aiBudget.setBudgetEndDate(endDate);
            
            // Extract specific category budgets
            aiBudget.setFood(extractAmount(budgetSection, "Food:"));
            aiBudget.setHousing(extractAmount(budgetSection, "Housing:"));
            aiBudget.setTransportation(extractAmount(budgetSection, "Transportation:"));
            aiBudget.setEntertainment(extractAmount(budgetSection, "Entertainment:"));
            
            // Extract additional insights
            String insights = response.split("Additional Insights:")[1].trim();
            
            return new AiSuggestion(insights, aiBudget);
        } catch (Exception e) {
            // Fallback to previous parsing method or handle error
            return new AiSuggestion("Unable to parse AI response", new Budget());
        }
    }
    
    // Helper method to extract amount from a specific line
    private double extractAmount(String text, String prefix) {
        try {
            String amountLine = Arrays.stream(text.split("\n"))
                .filter(line -> line.startsWith(prefix))
                .findFirst()
                .orElse(prefix + " 0");
            
            return Double.parseDouble(amountLine.replace(prefix, "").trim().replace("$", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    
    // Helper method to format withdrawal categories
    private String formatCategories(java.util.Map<String, Double> categories) {
        if (categories == null || categories.isEmpty()) return "No categories";
        
        return categories.entrySet().stream()
            .map(entry -> entry.getKey() + ": $" + String.format("%.2f", entry.getValue()))
            .collect(Collectors.joining(", "));
    }
    private LocalDate extractDate(String text, String prefix) {
        try {
            String dateLine = Arrays.stream(text.split("\n"))
                .filter(line -> line.startsWith(prefix))
                .findFirst()
                .orElse(prefix + " " + LocalDate.now());
            String dateString = dateLine.replace(prefix, "").trim();
            return LocalDate.parse(dateString);
        } catch (Exception e) {
            return LocalDate.now(); // Fallback to current date if parsing fails
        }
    }
}