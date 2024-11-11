package com.finance.transaction.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.finance.transaction.model.Transaction;

@FeignClient(name = "BUDGET-SERVICE")
public interface BudgetServiceClient {
    @PostMapping("/api/budgets/check-exceedance")
    ResponseEntity<String> checkExceedance(@RequestBody Transaction transaction);
}
