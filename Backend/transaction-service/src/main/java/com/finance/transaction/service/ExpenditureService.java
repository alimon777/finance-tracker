package com.finance.transaction.service;

import com.finance.transaction.dto.ExpenditureSummaryDTO;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;

@Service
public class ExpenditureService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Helper method to update deposit and withdrawal maps
    private void updateExpenditureMap(Map<String, Double> depositMap, Map<String, Double> withdrawMap, double amount, String category, TransactionType type) {
        if (type == TransactionType.WITHDRAW) {
            withdrawMap.put(category, withdrawMap.getOrDefault(category, 0.0) + amount);
        } else {
            depositMap.put(category, depositMap.getOrDefault(category, 0.0) + amount);
        }
    }

    // Helper method to get the current date information
    private Map<String, Integer> getCurrentDateInfo() {
        LocalDate today = LocalDate.now();
        Map<String, Integer> dateInfo = new HashMap<>();
        dateInfo.put("year", today.getYear());
        dateInfo.put("month", today.getMonthValue());
        dateInfo.put("week", today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        return dateInfo;
    }

    public ExpenditureSummaryDTO getExpenditureSummary(Long userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        ExpenditureSummaryDTO summary = new ExpenditureSummaryDTO();

        // Maps to store data categorized by period (week, month, year)
        Map<String, Double> weeklyDepositMap = new HashMap<>();
        Map<String, Double> weeklyWithdrawMap = new HashMap<>();
        Map<String, Double> monthlyDepositMap = new HashMap<>();
        Map<String, Double> monthlyWithdrawMap = new HashMap<>();
        Map<String, Double> yearlyDepositMap = new HashMap<>();
        Map<String, Double> yearlyWithdrawMap = new HashMap<>();

        double weeklyWithdrawTotal = 0.0;
        double monthlyWithdrawTotal = 0.0;
        double yearlyWithdrawTotal = 0.0;

        // Get current date information (year, month, week)
        Map<String, Integer> currentDateInfo = getCurrentDateInfo();
        int currentYear = currentDateInfo.get("year");
        int currentMonth = currentDateInfo.get("month");
        int currentWeek = currentDateInfo.get("week");

        // Process each transaction and update maps
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate();
            int transactionYear = transactionDate.getYear();
            int transactionMonth = transactionDate.getMonthValue();
            int transactionWeek = transactionDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

            String category = transaction.getCategoryType().name();
            double amount = transaction.getAmount();

            // Only process transactions for the current year
            if (transactionYear == currentYear) {
                // Process yearly expenditure
                updateExpenditureMap(yearlyDepositMap, yearlyWithdrawMap, amount, category, transaction.getTransactionType());
                if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                    yearlyWithdrawTotal += amount;
                }

                // Process monthly expenditure
                if (transactionMonth == currentMonth) {
                    updateExpenditureMap(monthlyDepositMap, monthlyWithdrawMap, amount, category, transaction.getTransactionType());
                    if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                        monthlyWithdrawTotal += amount;
                    }

                    // Process weekly expenditure
                    if (transactionWeek == currentWeek) {
                        updateExpenditureMap(weeklyDepositMap, weeklyWithdrawMap, amount, category, transaction.getTransactionType());
                        if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                            weeklyWithdrawTotal += amount;
                        }
                    }
                }
            }
        }

        // Set the data into the DTO
        summary.setWeekly(new ExpenditureSummaryDTO.ExpenditureDetail(weeklyDepositMap, weeklyWithdrawMap, weeklyWithdrawTotal));
        summary.setMonthly(new ExpenditureSummaryDTO.ExpenditureDetail(monthlyDepositMap, monthlyWithdrawMap, monthlyWithdrawTotal));
        summary.setYearly(new ExpenditureSummaryDTO.ExpenditureDetail(yearlyDepositMap, yearlyWithdrawMap, yearlyWithdrawTotal));

        return summary;
    }
}
