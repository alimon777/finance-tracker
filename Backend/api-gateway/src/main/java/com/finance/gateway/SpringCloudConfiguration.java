package com.finance.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;


@Configuration

public class SpringCloudConfiguration {

	@Bean
	RouteLocator gatewayRoutes(RouteLocatorBuilder builder)
	{
		return
				builder
				.routes()
				.route("user-service", r->r.path("/finwiz/**")
					.filters(f->f.addResponseHeader("X-Response-Header","user"))
					.uri("http://localhost:8001"))
				.route("transaction-service",r->r.path("/finwiz/**")
					.filters(f->f.addResponseHeader("X-Response-Header","transaction"))
					.uri("http://localhost:8002"))
				.route("budget-service",r->r.path("/finwiz/**")
						.filters(f->f.addResponseHeader("X-Response-Header","mail"))
						.uri("http://localhost:8003"))
				.route("goal-service",r->r.path("/finwiz/**")
						.filters(f->f.addResponseHeader("X-Response-Header","goal"))
						.uri("http://localhost:8004"))
				.route("mail-service",r->r.path("/finwiz/**")
						.filters(f->f.addResponseHeader("X-Response-Header","goal"))
						.uri("http://localhost:8005"))
				.build();
	}
}
