package com.finance.goal.service;

import com.finance.goal.dto.GoalDto;
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

//    public long calculateDurationInMonths(LocalDate startDate) {
//        LocalDate currentDate = LocalDate.now();
//        return ChronoUnit.MONTHS.between(startDate, currentDate);
//    }

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

/*
    public List<Goal> getAllGoals() {
        try {
            List<Goal> goals = goalRepository.findAll();
            logger.info("Successfully fetched all goals, count: {}", goals.size());
            return goals;
        } catch (Exception e) {
            logger.error("Error occurred while fetching goals", e);
            throw new GoalServiceException("Failed to fetch goals", e);
        }
    }*/

 /*   public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> {
                    logger.error("Goal not found with id: {}", goalId);
                    return new GoalNotFoundException("Goal not found with id: " + goalId);
                });
    }*/

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
