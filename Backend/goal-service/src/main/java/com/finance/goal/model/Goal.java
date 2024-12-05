package com.finance.goal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goals")
public class Goal {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private Long userId;
	    private String goalName;
	    private Double value;
	    private String description;
	    private LocalDate startDate;
	    private Integer durationInMonths;
	}

