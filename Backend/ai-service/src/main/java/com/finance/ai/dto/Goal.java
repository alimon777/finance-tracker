package com.finance.ai.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goal {
	    private Long id;
	    private Long userId;
	    private String goalName;
	    private Double value;
	    private String description;
	    private LocalDate startDate;
	    private Integer durationInMonths;
	}

