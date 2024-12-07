package com.finance.goal.controller;

import com.finance.goal.model.Goal;
import com.finance.goal.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;
    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        return ResponseEntity.ok(goalService.createGoal(goal));
    }
    
    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals(@RequestParam Long userId) {
        try {
            List<Goal> goals = goalService.getAllGoalsForUser(userId);       
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching goals for user " + userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable Long id, @RequestBody Goal updatedGoal) {
        Goal existingGoal = goalService.getGoalById(id);
        if (existingGoal == null) {
            return ResponseEntity.notFound().build();
        }

        existingGoal.setGoalName(updatedGoal.getGoalName());
        existingGoal.setValue(updatedGoal.getValue());
        existingGoal.setDescription(updatedGoal.getDescription());
        existingGoal.setDurationInMonths(updatedGoal.getDurationInMonths());
        existingGoal.setStartDate(updatedGoal.getStartDate());

        Goal savedGoal = goalService.saveGoal(existingGoal);
        return ResponseEntity.ok(savedGoal);
    }
}
