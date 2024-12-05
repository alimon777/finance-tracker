package com.finance.ai.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

