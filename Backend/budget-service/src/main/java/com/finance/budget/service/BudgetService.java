package com.finance.budget.service;

import com.finance.budget.client.EmailServiceClient;
import com.finance.budget.dto.EmailDetails;
import com.finance.budget.dto.Transaction;
import com.finance.budget.model.Budget;
import com.finance.budget.repository.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepo budgetRepository;
    
    @Autowired
    private EmailServiceClient emailServiceClient;
    
    private Double calculateTotal(Budget budget) {
    	return (budget.getFood() != null ? budget.getFood() : 0) +
    	       (budget.getHousing() != null ? budget.getHousing() : 0) +
    	       (budget.getTransportation() != null ? budget.getTransportation() : 0) +
    	       (budget.getEntertainment() != null ? budget.getEntertainment() : 0);
    	    
    }

    // Create a new budget
    public Budget createBudget(Budget budget) {
    	budget.setTotal(calculateTotal(budget));
        return budgetRepository.save(budget);
    }

    // Get all budgets for a specific user
    public List<Budget> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    // Get budget by ID
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }
    
    // Delete budget
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
    
    public String checkExceedance(Transaction transaction) {
    	List<Budget> budgetList = budgetRepository.findBudgetsByDateRangeAndNonNegativeTotalForUser(
                transaction.getTransactionDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(), transaction.getUserId());
    	
    	if(budgetList.isEmpty()) {
    		return "No Budget exceeded in this category";
    	}
        StringBuilder emailBody = new StringBuilder("Dear " + budgetList.get(0).getUsername() + ",\n\n");
        emailBody.append("The following transaction on ")
                 .append(transaction.getTransactionDate())
                 .append(" caused your budget to exceed in specific categories:\n\n");

        List<Budget> exceededBudgetList = new ArrayList<>();
        
        for (Budget budget : budgetList) {
            if (budget.getTotal() >= 0) {
                Double initialAmount;
                boolean categoryExceeded = false;
                boolean totalExceeded = false;

                switch (transaction.getCategoryType()) {
                    case FOOD:
                        initialAmount = budget.getFood();
                        budget.setFood(initialAmount - transaction.getAmount());
                        if (initialAmount >= 0 && budget.getFood() < 0) categoryExceeded = true;
                        break;

                    case HOUSING:
                        initialAmount = budget.getHousing();
                        budget.setHousing(initialAmount - transaction.getAmount());
                        if (initialAmount >= 0 && budget.getHousing() < 0) categoryExceeded = true;
                        break;

                    case TRANSPORTATION:
                        initialAmount = budget.getTransportation();
                        budget.setTransportation(initialAmount - transaction.getAmount());
                        if (initialAmount >= 0 && budget.getTransportation() < 0) categoryExceeded = true;
                        break;

                    case ENTERTAINMENT:
                        initialAmount = budget.getEntertainment();
                        budget.setEntertainment(initialAmount - transaction.getAmount());
                        if (initialAmount >= 0 && budget.getEntertainment() < 0) categoryExceeded = true;
                        break;

                    default:
                        break;
                }

                // Check if total budget exceeded
                budget.setTotal(budget.getTotal() - transaction.getAmount());
                if (budget.getTotal() < 0) totalExceeded = true;

                if (categoryExceeded || totalExceeded) {
                    exceededBudgetList.add(budget);
                    budgetRepository.save(budget);

                    if (categoryExceeded) {
                        emailBody.append("Category: ").append(transaction.getCategoryType())
                                 .append(" exceeded by ");
                        switch (transaction.getCategoryType()) {
                        	case FOOD:
                                 emailBody.append(Math.abs(budget.getFood()));
                                 break;
                        	case ENTERTAINMENT:
                        		 emailBody.append(Math.abs(budget.getEntertainment()));
                        		 break;
                        	case HOUSING:
                        		 emailBody.append(Math.abs(budget.getHousing()));
                        		 break;
                        	case TRANSPORTATION:
                        		 emailBody.append(Math.abs(budget.getTransportation()));
                        		 break;
                        	default:
                        		 break;
                        }
                        emailBody.append(" on transaction date: ")
                                 .append(transaction.getTransactionDate())
                                 .append("\n");
                    }
                    if (totalExceeded) {
                        emailBody.append("Total budget exceeded by ")
                                 .append(Math.abs(budget.getTotal()))
                                 .append(" for the period ")
                                 .append(budget.getBudgetStartDate())
                                 .append(" to ")
                                 .append(budget.getBudgetEndDate())
                                 .append("\n");
                    }
                }
            }
        }

        // Send email if budget exceeded
        if (!exceededBudgetList.isEmpty()) {
            EmailDetails emailDetails = new EmailDetails(
                    exceededBudgetList.get(0).getEmail(),
                    emailBody.toString(),
                    "Budget Exceeded Notification"
            );
            emailServiceClient.sendMail(emailDetails);
        }

        return emailBody.toString();
    }

}
