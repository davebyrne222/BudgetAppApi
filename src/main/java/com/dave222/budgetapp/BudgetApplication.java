package com.dave222.budgetapp;

import com.dave222.budgetapp.config.CorsConfig;
import com.dave222.budgetapp.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgetApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BudgetApplication.class);
		app.addInitializers(new DotenvConfig());
		app.run(args);
	}
}