package com.finance.goal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finance.goal.model.Goal;
import com.finance.goal.service.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GoalControllerTest {

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalController goalController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(goalController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    
    @Test
    public void testCreateGoal() throws Exception {
        Goal goal = new Goal(1L, 1L, "Buy Car", 10000.0, "Save money for a car",LocalDate.now(), 12);
        when(goalService.createGoal(any(Goal.class))).thenReturn(goal);

        mockMvc.perform(post("/api/goals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.goalName").value("Buy Car"))
                .andExpect(jsonPath("$.value").value(10000.0))
                .andExpect(jsonPath("$.description").value("Save money for a car"));
    }

    @Test
    public void testGetAllGoalsForUser() throws Exception {
        Long userId = 102L;
        Goal goal1 = new Goal(1L, userId, "Buy Car", 10000.0, "Save money for a car", LocalDate.now(), 12);
        Goal goal2 = new Goal(2L, userId, "Buy House", 50000.0, "Save money for a house", LocalDate.now(), 24);
        List<Goal> goals = Arrays.asList(goal1, goal2);
        when(goalService.getAllGoalsForUser(userId)).thenReturn(goals);

        mockMvc.perform(get("/api/goals/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].goalName").value("Buy Car"))
                .andExpect(jsonPath("$[1].goalName").value("Buy House"));
    }

    @Test
    public void testGetAllGoalsForUserNotFound() throws Exception {
        Long userId = 1L;
        when(goalService.getAllGoalsForUser(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/goals/user/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("[]"));
    }

    @Test
    public void testDeleteGoal() throws Exception {
        Long goalId = 1L;
        doNothing().when(goalService).deleteGoal(goalId);

        mockMvc.perform(delete("/api/goals/delete/{id}", goalId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateGoal() throws Exception {
        Long goalId = 1L;
        Goal existingGoal = new Goal(goalId, 1L, "Buy Car", 10000.0, "Save money for a car", LocalDate.now(), 12);
        Goal updatedGoal = new Goal(goalId, 1L, "Buy Car Updated", 12000.0, "Save more money for a car", LocalDate.now(), 14);

        when(goalService.getGoalById(goalId)).thenReturn(existingGoal);
        when(goalService.saveGoal(any(Goal.class))).thenReturn(updatedGoal);

        mockMvc.perform(put("/api/goals/update/{id}", goalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedGoal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalName").value("Buy Car Updated"))
                .andExpect(jsonPath("$.value").value(12000.0))
                .andExpect(jsonPath("$.description").value("Save more money for a car"))
                .andExpect(jsonPath("$.durationInMonths").value(14));
    }

    @Test
    public void testUpdateGoalNotFound() throws Exception {
        Long goalId = 1L;
        Goal updatedGoal = new Goal(goalId, 1L, "Buy Car Updated", 12000.0, "Save more money for a car", LocalDate.now(), 14);

        when(goalService.getGoalById(goalId)).thenReturn(null);

        mockMvc.perform(put("/api/goals/update/{id}", goalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedGoal)))
                .andExpect(status().isNotFound());
    }
}
