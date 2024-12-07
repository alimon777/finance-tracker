package com.finance.budget.service;

import com.finance.budget.client.EmailServiceClient;
import com.finance.budget.dto.EmailDetails;
import com.finance.budget.dto.Transaction;
import com.finance.budget.exceptions.InvalidBudgetException;
import com.finance.budget.exceptions.ResourceNotFoundException;
import com.finance.budget.model.Budget;
import com.finance.budget.repository.BudgetRepo;
import com.finance.budget.dto.CategoryType;
import com.finance.budget.dto.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @Mock
    private BudgetRepo budgetRepository;

    @Mock
    private EmailServiceClient emailServiceClient;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBudget_withValidBudget_shouldReturnSavedBudget() {
        Budget budget = new Budget(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(30), 100.0, 200.0, 150.0, 100.0, 550.0, false);

        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget createdBudget = budgetService.createBudget(budget);

        assertNotNull(createdBudget);
        assertEquals(550.0, createdBudget.getTotal());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testCreateBudget_withNullBudgetField_shouldThrowInvalidBudgetException() {
        Budget budget = new Budget(null,null,LocalDate.now(), LocalDate.now().plusDays(30), null, null, null, null, null, false);

        assertThrows(InvalidBudgetException.class, () -> budgetService.createBudget(budget));
    }

    @Test
    void testGetBudgetsByUserId_withValidUserId_shouldReturnBudgetList() {
        Long userId = 1L;
        List<Budget> budgets = Arrays.asList(new Budget(1L, userId, LocalDate.now(), LocalDate.now().plusDays(30), 100.0, 200.0, 150.0, 100.0, 550.0, false));

        when(budgetRepository.findByUserId(userId)).thenReturn(budgets);

        List<Budget> foundBudgets = budgetService.getBudgetsByUserId(userId);

        assertEquals(1, foundBudgets.size());
        assertEquals(userId, foundBudgets.get(0).getUserId());
    }

    @Test
    void testGetBudgetById_withValidId_shouldReturnBudget() {
        Long budgetId = 1L;
        Budget budget = new Budget(budgetId, 1L, LocalDate.now(), LocalDate.now().plusDays(30), 100.0, 200.0, 150.0, 100.0, 550.0, false);

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        Optional<Budget> foundBudget = budgetService.getBudgetById(budgetId);

        assertTrue(foundBudget.isPresent());
        assertEquals(budgetId, foundBudget.get().getId());
    }

    @Test
    void testGetBudgetById_withInvalidId_shouldThrowResourceNotFoundException() {
        Long budgetId = 1L;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> budgetService.getBudgetById(budgetId));
    }

    @Test
    void testDeleteBudget_withValidId_shouldDeleteBudget() {
        Long budgetId = 1L;
        Budget budget = new Budget(budgetId, 1L, LocalDate.now(), LocalDate.now().plusDays(30), 100.0, 200.0, 150.0, 100.0, 550.0, false);

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        doNothing().when(budgetRepository).deleteById(budgetId);

        budgetService.deleteBudget(budgetId);

        verify(budgetRepository, times(1)).deleteById(budgetId);
    }

    @Test
    void testDeleteBudget_withInvalidId_shouldThrowResourceNotFoundException() {
        Long budgetId = 1L;

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> budgetService.deleteBudget(budgetId));
    }

    @Test
    void testCheckExceedance_withExceededBudget_shouldSendEmailAndReturnMessage() {
        Long budgetId = 1L;
    	Long userId = 1L;
        Transaction transaction = new Transaction(1L, 300.0, userId, "Food Purchase", "ACC123", LocalDate.now(), TransactionType.WITHDRAW, CategoryType.FOOD);
        
        Budget budget = new Budget(budgetId, 1L, LocalDate.now(), LocalDate.now().plusDays(30), 100.0, 200.0, 150.0, 100.0, 550.0, false);
        
        when(budgetRepository.findBudgetsByDateRangeAndNonNegativeTotalForUser(LocalDate.now(), userId)).thenReturn(Collections.singletonList(budget));

        when(emailServiceClient.sendMail(any(EmailDetails.class))).thenReturn(null);

        String result = budgetService.checkExceedance(transaction);

        assertTrue(result.contains("budget to exceed"));
        verify(emailServiceClient, times(1)).sendMail(any(EmailDetails.class));
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testCheckExceedance_withNonExceededBudget_shouldNotSendEmail() {
    	Long budgetId = 1L;
        Long userId = 1L;
        Transaction transaction = new Transaction(1L, 50.0, userId, "Food Purchase", "ACC123", LocalDate.now(), TransactionType.WITHDRAW, CategoryType.FOOD);
        
        Budget budget = new Budget(budgetId, 1L, LocalDate.now(), LocalDate.now().plusDays(30), 100.0, 200.0, 150.0, 100.0, 550.0, false);
        
        when(budgetRepository.findBudgetsByDateRangeAndNonNegativeTotalForUser(LocalDate.now(), userId)).thenReturn(Collections.singletonList(budget));

        String result = budgetService.checkExceedance(transaction);

        assertFalse(result.contains("exceeded"));
        verify(emailServiceClient, never()).sendMail(any(EmailDetails.class));
        verify(budgetRepository, times(1)).save(budget);
    }
}
