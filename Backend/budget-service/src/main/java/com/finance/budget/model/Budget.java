package com.finance.budget.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String username;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate budgetStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate budgetEndDate;    
    private Double food;  
    private Double housing;  
    private Double transportation; 
    private Double entertainment;  
    private Double total;  
    private Boolean aiGenerated;
}
