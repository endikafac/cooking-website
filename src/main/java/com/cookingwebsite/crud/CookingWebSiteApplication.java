package com.cookingwebsite.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;

@OpenAPIDefinition(info = @Info(title = "CookingWebSite API Rest ", version = "0.1.0", description = "API Rest service for Cooking Web Site application"))

@SpringBootApplication
@Slf4j
public class CookingWebSiteApplication extends SpringBootServletInitializer {
	
	public static void main(final String[] args) {

		log.info("Starting to run the CookingWebSite application...");
		SpringApplication.run(CookingWebSiteApplication.class, args);
		log.info("CookingWebSite application is up correctly.");
	}
	
}
