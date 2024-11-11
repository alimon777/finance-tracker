package com.finance.budget.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // User ID
    private LocalDate budgetStartDate;  // Budget start date
    private LocalDate budgetEndDate;    // Budget end date

    private Double food;  // Major budget category for food
    private Double housing;  // Major budget category for housing
    private Double transportation;  // Major budget category for transportation
    private Double entertainment;  // Major budget category for entertainment
    private Boolean aiGenerated;
    private Double total;  // Indicates if budget is exceeded

}
