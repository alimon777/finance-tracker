package com.finance.ai.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finance.ai.dto.Goal;

@FeignClient(name = "GOAL-SERVICE")
public interface GoalServiceClient {
	 @GetMapping("api/goals")
	 ResponseEntity<List<Goal>> getAllGoals(@RequestParam Long userId) ;
}
