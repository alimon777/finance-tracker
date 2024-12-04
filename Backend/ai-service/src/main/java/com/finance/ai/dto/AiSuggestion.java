package com.finance.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiSuggestion {
	private String textContent;
	private Budget budget;
	
}
