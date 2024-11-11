package com.finance.goal.service;


import com.finance.goal.exception.GoalNotFoundException;
import com.finance.goal.exception.GoalServiceException;
import com.finance.goal.model.Goal;
import com.finance.goal.repository.GoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalService {

    private static final Logger logger = LoggerFactory.getLogger(GoalService.class);
    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }
    
    public Goal saveGoal(Goal goal)
    {
    	return goalRepository.save(goal);
    }


    public Goal createGoal(Goal goal) {
        try {
            LocalDate startDate = LocalDate.now();
            goal.setStartDate(startDate);

            return goalRepository.save(goal);
        } catch (Exception e) {
            logger.error("Error creating goal: ", e);  // Log the error with the exception details
            throw new GoalServiceException("Failed to create goal", e);
        }
    }


    public long calculateRemainingDays(Goal goal) {
        LocalDate endDate = goal.getStartDate().plusMonths(goal.getDurationInMonths());
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public List<String> getGoalNotifications() {
        List<Goal> goals = goalRepository.findAll();
        List<String> notifications = new ArrayList<>();

        for (Goal goal : goals) {
            long remainingDays = calculateRemainingDays(goal);
            if (remainingDays >= 0) {
                notifications.add("Goal: " + goal.getGoalName());
                notifications.add("Value: " + goal.getValue() );
                notifications.add("Days Left: " + remainingDays);
            } else {
                notifications.add("Goal: " + goal.getGoalName() + " - Goal Duration Expired!");
            }
        }
        return notifications;
    }

    public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> {
                    logger.error("Goal not found with id: {}", goalId);
                    return new GoalNotFoundException("Goal not found with id: " + goalId);
                });
    }

    public void deleteGoal(Long goalId) {
        if (!goalRepository.existsById(goalId)) {
            logger.error("Goal not found with id: {}", goalId);
            throw new GoalNotFoundException("Goal not found with id: " + goalId);
        }
        goalRepository.deleteById(goalId);
    }
    
    public List<Goal> getAllGoalsForUser(Long userId) {
        return goalRepository.findByUserId(userId);
    }
}
