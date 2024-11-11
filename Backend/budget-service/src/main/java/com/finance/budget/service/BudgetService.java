package com.finance.budget.service;

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
    
    private Double calculateTotal(Budget budget) {
//    	return budget.getFood()+budget.getEntertainment()+budget.getHousing()+budget.getTransportation();
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

    // Get all budgets
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
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
    	String result = "Budget List : ";
    	List<Budget> budgetList = budgetRepository.findBudgetsByDateRangeAndNonNegativeTotalForUser(
                transaction.getTransactionDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),transaction.getUserId());

       List<Budget> exceededBudgetList = new ArrayList<>();
       List<Transaction> exceededTransactionList = new ArrayList<>();

            // Step 2: Loop through each budget and apply the transaction to the relevant category
       for (Budget budget : budgetList) {
            if(budget.getTotal()>=0) {
            	Double amount = 0d;
                switch (transaction.getCategoryType()) {
                    case FOOD:
                    	budget.setTotal(budget.getTotal() - transaction.getAmount());
                    	amount=budget.getFood();
                    	budget.setFood(budget.getFood() - transaction.getAmount());	
                    	if(amount>=0) {
                    		if(budget.getFood()<0 || budget.getTotal()<0) {
                        		exceededBudgetList.add(budget);
                                exceededTransactionList.add(transaction);
                        	}
                    	}
                        break;
                    case HOUSING:
                    	budget.setTotal(budget.getTotal() - transaction.getAmount());
                    	amount=budget.getHousing();
                    	budget.setHousing(budget.getHousing() - transaction.getAmount());
                    	if(amount>=0) {                    		
                    		if(budget.getHousing()<0 || budget.getTotal()<0) {
                        		exceededBudgetList.add(budget);
                                exceededTransactionList.add(transaction);
                        	}
                    	}
                        break;
                    case TRANSPORTATION:
                    	budget.setTotal(budget.getTotal() - transaction.getAmount());
                    	amount= budget.getTransportation();
                    	budget.setTransportation(budget.getTransportation() - transaction.getAmount());
                    	if(amount>=0) {                 		
                    		if(budget.getTransportation()<0 || budget.getTotal()<0) {
                        		exceededBudgetList.add(budget);
                                exceededTransactionList.add(transaction);
                        	}
                    	}
                    	break;
                    case ENTERTAINMENT:
                    	budget.setTotal(budget.getTotal() - transaction.getAmount());
                    	amount=budget.getEntertainment();
                    	budget.setEntertainment(budget.getEntertainment() - transaction.getAmount());
                    	if(amount>=0) {
                    		if(budget.getEntertainment()<0 || budget.getTotal()<0) {
                        		exceededBudgetList.add(budget);
                                exceededTransactionList.add(transaction);
                        	}
                    	}
                        break;
                    default:
                        break;
                }budgetRepository.save(budget);
            }
        }

        if (!budgetList.isEmpty()) {
        	for(Budget budget:budgetList) {
           	 	result+=budget.toString()+"\n";
            }
        	result+="Exceeded budget list :";
        	for(Budget budget:exceededBudgetList) {
           	 	result+=budget.toString()+"\n";
            }
        }
    	return result;
    }
}
