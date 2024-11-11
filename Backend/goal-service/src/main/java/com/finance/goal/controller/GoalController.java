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

    @PostMapping("/create")
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        return ResponseEntity.ok(goalService.createGoal(goal));
    }

//    @GetMapping("/notifications")
//    public ResponseEntity<List<String>> getGoalNotifications() {
//        List<String> notifications = goalService.getGoalNotifications();
//        return ResponseEntity.ok(notifications);
//    }

//    @GetMapping("/all")
//    public ResponseEntity<List<Goal>> getAllGoals() {
//        try {
//            List<Goal> goals = goalService.getAllGoals();
//            return ResponseEntity.ok(goals);
//        } catch (Exception e) {
//            // Log the exception and return a 500 response if an error occurs
//            logger.error("An unexpected error occurred", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Goal>> getAllGoals(@PathVariable Long userId) {
        try {
            List<Goal> goals = goalService.getAllGoalsForUser(userId);
            if (goals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(goals);
            }       
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching goals for user " + userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
