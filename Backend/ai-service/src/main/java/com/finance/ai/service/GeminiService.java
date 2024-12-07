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
import com.finance.ai.exception.ApiClientException;
import com.finance.ai.exception.BudgetParsingException;
import com.finance.ai.exception.InvalidDataException;

@Service
public class GeminiService {

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private ExpenditureServiceClient expenditureServiceClient;

    @Autowired
    private GoalServiceClient goalServiceClient;

    public String generateText(String prompt) {
        try {
            return geminiClient.generateTextWithSpring(prompt);
        } catch (Exception e) {
            throw new ApiClientException("Failed to generate text with GeminiClient", e);
        }
    }

    public String generatePrompt(Long userId) {
        try {
            ExpenditureSummaryDTO expenditureSummary = expenditureServiceClient.getExpenditureSummary(userId).getBody();
            if (expenditureSummary == null) {
                throw new InvalidDataException("Expenditure summary is null for user " + userId);
            }

            List<Goal> goals = goalServiceClient.getAllGoals(userId).getBody();
            if (goals == null || goals.isEmpty()) {
                throw new InvalidDataException("Goals data is invalid or empty for user " + userId);
            }

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
            promptBuilder.append("\nPlease provide a detailed budget suggestion in the following EXACT format...\n");

            return promptBuilder.toString();
        } catch (Exception e) {
            throw new ApiClientException("Failed to generate prompt for user " + userId, e);
        }
    }

    public AiSuggestion processResponse(String response) {
        Budget aiBudget = new Budget();
        aiBudget.setAiGenerated(true);
        try {
            String budgetSection = response.split("BUDGET_SUGGESTION_START")[1].split("BUDGET_SUGGESTION_END")[0];

            // Extract start and end dates
            LocalDate startDate = extractDate(budgetSection, "Start Date:");
            LocalDate endDate = extractDate(budgetSection, "End Date:");
            aiBudget.setBudgetStartDate(startDate);
            aiBudget.setBudgetEndDate(endDate);

            // Extract category budgets
            aiBudget.setFood(extractAmount(budgetSection, "Food:"));
            aiBudget.setHousing(extractAmount(budgetSection, "Housing:"));
            aiBudget.setTransportation(extractAmount(budgetSection, "Transportation:"));
            aiBudget.setEntertainment(extractAmount(budgetSection, "Entertainment:"));

            // Extract insights
            String insights = response.split("Additional Insights:")[1].trim();

            return new AiSuggestion(insights, aiBudget);
        } catch (Exception e) {
            throw new BudgetParsingException("Error while parsing the AI-generated budget response", e);
        }
    }

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
