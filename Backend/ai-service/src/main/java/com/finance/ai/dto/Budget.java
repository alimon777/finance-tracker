package com.finance.ai.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Budget {
    private Long id;
    private Long userId;
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
