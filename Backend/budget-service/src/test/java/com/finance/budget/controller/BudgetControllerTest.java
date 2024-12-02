package com.finance.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finance.budget.dto.CategoryType;
import com.finance.budget.dto.Transaction;
import com.finance.budget.dto.TransactionType;
import com.finance.budget.model.Budget;
import com.finance.budget.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateBudget() throws Exception {

        Budget budget = new Budget(null, 1L, LocalDate.now(), LocalDate.now().plusMonths(1), 500.0, 1000.0, 300.0, 200.0, 0.0, false);
        Budget savedBudget = new Budget(1L, 1L, LocalDate.now(), LocalDate.now().plusMonths(1), 500.0, 1000.0, 300.0, 200.0, 2000.0, false);

        when(budgetService.createBudget(any(Budget.class))).thenReturn(savedBudget);

 
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.total").value(2000.0));
    }

    @Test
    public void testGetBudgetsByUserId() throws Exception {

        Budget budget1 = new Budget(1L, 1L, LocalDate.now(), LocalDate.now().plusMonths(1), 500.0, 1000.0, 300.0, 200.0, 2000.0, false);
        Budget budget2 = new Budget(2L, 1L, LocalDate.now(), LocalDate.now().plusMonths(1), 600.0, 1100.0, 350.0, 210.0, 2260.0, false);
        
        when(budgetService.getBudgetsByUserId(1L)).thenReturn(Arrays.asList(budget1, budget2));

        mockMvc.perform(get("/api/budgets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void testDeleteBudget() throws Exception {
        doNothing().when(budgetService).deleteBudget(1L);

        mockMvc.perform(delete("/api/budgets/1/1"))
                .andExpect(status().isNoContent());

        verify(budgetService, times(1)).deleteBudget(1L);
    }

    @Test
    public void testCheckExceedance() throws Exception {

    	Transaction transaction = new Transaction(1L, 200.0, 1L, "Food purchase", "ACCNO1", LocalDate.now(), TransactionType.WITHDRAW, CategoryType.FOOD);

        Budget budget = new Budget(1L, 1L, LocalDate.now(), LocalDate.now().plusMonths(1), 500.0, 1000.0, 300.0, 200.0, 0.0, false);
        when(budgetService.checkExceedance(transaction)).thenReturn("Budget exceeded in Food category");

        mockMvc.perform(post("/api/budgets/check-exceedance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().string("Budget exceeded in Food category"));
    }

    @Test
    public void testCheckNonExceedance() throws Exception {
    	Transaction transaction = new Transaction(1L, 200.0, 1L, "Food purchase", "ACCNO1", LocalDate.now(), TransactionType.WITHDRAW, CategoryType.FOOD);

        Budget budget = new Budget(1L, 1L, LocalDate.now(), LocalDate.now().plusMonths(1), 500.0, 1000.0, 300.0, 200.0, 0.0, false);
        when(budgetService.checkExceedance(transaction)).thenReturn("No exceedance in the budget.");

        mockMvc.perform(post("/api/budgets/check-exceedance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().string("No exceedance in the budget."));
    }
}
