package com.finance.transaction.service;

import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.TransactionRepository;
import com.finance.transaction.dto.ExpenditureSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenditureService {

    @Autowired
    private TransactionRepository transactionRepository;

    public ExpenditureSummaryDTO getExpenditureSummary(Long userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        ExpenditureSummaryDTO summary = new ExpenditureSummaryDTO();

        // Prepare data for weekly, monthly, and yearly expenditures
        Map<String, Double> weeklyExpenditureMap = new HashMap<>();
        Map<String, Double> monthlyExpenditureMap = new HashMap<>();
        Map<String, Double> yearlyExpenditureMap = new HashMap<>();

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        // Iterate over all transactions and categorize them
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                calendar.setTime(transaction.getTransactionDate());
                int transactionYear = calendar.get(Calendar.YEAR);
                int transactionMonth = calendar.get(Calendar.MONTH);
                int transactionWeek = calendar.get(Calendar.WEEK_OF_YEAR);

                // Weekly Expenditure
                if (transactionYear == currentYear && transactionWeek == currentWeek) {
                    String category = transaction.getCategoryType().name();
                    weeklyExpenditureMap.put(category, weeklyExpenditureMap.getOrDefault(category, 0.0) + transaction.getAmount());
                }

                // Monthly Expenditure
                if (transactionYear == currentYear && transactionMonth == currentMonth) {
                    String category = transaction.getCategoryType().name();
                    monthlyExpenditureMap.put(category, monthlyExpenditureMap.getOrDefault(category, 0.0) + transaction.getAmount());
                }

                // Yearly Expenditure
                if (transactionYear == currentYear) {
                    String category = transaction.getCategoryType().name();
                    yearlyExpenditureMap.put(category, yearlyExpenditureMap.getOrDefault(category, 0.0) + transaction.getAmount());
                }
            }
        }

        // Set the values in the DTO
        summary.setWeeklyExpenditure(new ExpenditureSummaryDTO.ExpenditureDetail(weeklyExpenditureMap));
        summary.setMonthlyExpenditure(new ExpenditureSummaryDTO.ExpenditureDetail(monthlyExpenditureMap));
        summary.setYearlyExpenditure(new ExpenditureSummaryDTO.ExpenditureDetail(yearlyExpenditureMap));

        return summary;
    }
}
