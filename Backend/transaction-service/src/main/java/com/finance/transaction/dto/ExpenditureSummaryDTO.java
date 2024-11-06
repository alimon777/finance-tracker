package com.finance.transaction.dto;

import java.util.HashMap;
import java.util.Map;

public class ExpenditureSummaryDTO {

    // Weekly expenditure details
    private ExpenditureDetail weeklyExpenditure;

    // Monthly expenditure details
    private ExpenditureDetail monthlyExpenditure;

    // Yearly expenditure details
    private ExpenditureDetail yearlyExpenditure;

    // Constructor, getters, and setters

    public ExpenditureSummaryDTO() {
        // Default constructor
    }

    public ExpenditureSummaryDTO(ExpenditureDetail weeklyExpenditure, ExpenditureDetail monthlyExpenditure, ExpenditureDetail yearlyExpenditure) {
        this.weeklyExpenditure = weeklyExpenditure;
        this.monthlyExpenditure = monthlyExpenditure;
        this.yearlyExpenditure = yearlyExpenditure;
    }

    public ExpenditureDetail getWeeklyExpenditure() {
        return weeklyExpenditure;
    }

    public void setWeeklyExpenditure(ExpenditureDetail weeklyExpenditure) {
        this.weeklyExpenditure = weeklyExpenditure;
    }

    public ExpenditureDetail getMonthlyExpenditure() {
        return monthlyExpenditure;
    }

    public void setMonthlyExpenditure(ExpenditureDetail monthlyExpenditure) {
        this.monthlyExpenditure = monthlyExpenditure;
    }

    public ExpenditureDetail getYearlyExpenditure() {
        return yearlyExpenditure;
    }

    public void setYearlyExpenditure(ExpenditureDetail yearlyExpenditure) {
        this.yearlyExpenditure = yearlyExpenditure;
    }

    // Nested static class to hold category-wise expenditure details
    public static class ExpenditureDetail {

        // A map to hold category-wise expenditure (e.g., Groceries, Rent, etc.)
        private Map<String, Double> categoryWiseExpenditure;

        // Default constructor
        public ExpenditureDetail() {
            this.categoryWiseExpenditure = new HashMap<>();
        }

        // Constructor with categoryWiseExpenditure
        public ExpenditureDetail(Map<String, Double> categoryWiseExpenditure) {
            this.categoryWiseExpenditure = categoryWiseExpenditure;
        }

        // Getter for categoryWiseExpenditure
        public Map<String, Double> getCategoryWiseExpenditure() {
            return categoryWiseExpenditure;
        }

        // Setter for categoryWiseExpenditure
        public void setCategoryWiseExpenditure(Map<String, Double> categoryWiseExpenditure) {
            this.categoryWiseExpenditure = categoryWiseExpenditure;
        }
    }
}
