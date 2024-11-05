package com.finance.goal.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalDto {

	    private String goalName;
	    private Double value;
	    private String description;
	    private LocalDate startDate;
	    private Integer durationInMonths;
}