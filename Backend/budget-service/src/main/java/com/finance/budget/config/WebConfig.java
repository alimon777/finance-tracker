//package com.finance.budget.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/test/**")  // Adjust this to match your API
//                .allowedOrigins("http://localhost:4200") // Allow your frontend origin
//                .allowedMethods("GET", "POST","DELETE","PUT", "OPTIONS") // Allow these methods
//                .allowedHeaders("Content-Type","Authorization") // Allow the Authorization header
//                .allowCredentials(true); // Allow credentials if needed
//    }
//}
//
