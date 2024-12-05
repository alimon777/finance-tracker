package com.finance.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finance.ai.dto.ExpenditureSummaryDTO;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface ExpenditureServiceClient {
	@GetMapping("/api/transactions/summary")
    ResponseEntity<ExpenditureSummaryDTO> getExpenditureSummary(@RequestParam Long userId);
}
