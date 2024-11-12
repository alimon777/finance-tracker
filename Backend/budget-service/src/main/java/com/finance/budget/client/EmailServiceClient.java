package com.finance.budget.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.finance.budget.dto.EmailDetails;

// Define a Feign client to communicate with the Email Service
@FeignClient(name = "MAIL-SERVICE", path = "/api/mails") // "email-service" is the registered name in Eureka or application properties
public interface EmailServiceClient {

    @PostMapping("/sendMail")
    String sendMail(@RequestBody EmailDetails details);
}
