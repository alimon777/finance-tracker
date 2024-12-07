package com.finance.goal.service;

import com.finance.goal.exception.GoalNotFoundException;
import com.finance.goal.exception.GoalServiceException;
import com.finance.goal.model.Goal;
import com.finance.goal.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    private Goal testGoal;

    @BeforeEach
    public void setUp() {
        testGoal = new Goal();
        testGoal.setId(1L);
        testGoal.setUserId(1L);
        testGoal.setGoalName("Test Goal");
        testGoal.setValue(1000.0);
        testGoal.setStartDate(LocalDate.now());
        testGoal.setDurationInMonths(12);
    }

    @Test
    public void testCreateGoal() {
        when(goalRepository.save(Mockito.any(Goal.class))).thenReturn(testGoal);
        Goal createdGoal = goalService.createGoal(testGoal);
        assertNotNull(createdGoal);
        assertEquals("Test Goal", createdGoal.getGoalName());
        assertEquals(1000.0, createdGoal.getValue());
    }

    @Test
    public void testGetGoalById() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoal));
        Goal fetchedGoal = goalService.getGoalById(1L);
        assertNotNull(fetchedGoal);
        assertEquals(1L, fetchedGoal.getId());
        assertEquals("Test Goal", fetchedGoal.getGoalName());
    }

    @Test
    public void testGetGoalByIdNotFound() {
        when(goalRepository.findById(999L)).thenReturn(Optional.empty());
        GoalNotFoundException exception = assertThrows(GoalNotFoundException.class, () -> goalService.getGoalById(999L));
        assertEquals("Goal not found with id: 999", exception.getMessage());
    }

    @Test
    public void testDeleteGoal() {
        when(goalRepository.existsById(1L)).thenReturn(true);
        goalService.deleteGoal(1L);
        verify(goalRepository).deleteById(1L);
    }

    @Test
    public void testDeleteGoalNotFound() {
        when(goalRepository.existsById(999L)).thenReturn(false);
        GoalNotFoundException exception = assertThrows(GoalNotFoundException.class, () -> goalService.deleteGoal(999L));
        assertEquals("Goal not found with id: 999", exception.getMessage());
    }

    @Test
    public void testCalculateRemainingDays() {
        testGoal.setStartDate(LocalDate.now().minusMonths(3));
        testGoal.setDurationInMonths(6);
        long remainingDays = goalService.calculateRemainingDays(testGoal);
        assertTrue(remainingDays > 0);
    }

    @Test
    public void testCreateGoalException() {
        when(goalRepository.save(Mockito.any(Goal.class))).thenThrow(new RuntimeException("Database error"));
        GoalServiceException exception = assertThrows(GoalServiceException.class, () -> goalService.createGoal(testGoal));
        assertEquals("Failed to create goal", exception.getMessage());
    }
}
