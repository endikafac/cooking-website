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

//		StringBuilder body = new StringBuilder("");
//		body.append("<p>Dear Endika,&nbsp;</p>");
//		body.append("<p>Welcome to <strong>Cooking Web Site</strong>. We are providing you with your temporary password - \"XXX\".</p>");
//		body.append(
//				"<p>To access the application you can click <a href=\"http://localhost:4200/cooking-website/login\" target=\"_blank\" rel=\"noopener\">here</a>.</p>");
//		body.append("<p>Thanks a lot,</p>");
//		body.append("<p>Best regards,</p>");
//		body.append("<p></p>");
//		body.append("<p><strong>Cooking Web Site Team</strong></p>");
//		List<String> to = new ArrayList<String>();
//		to.add("endika.fernandez@twtspain.com");
//		to.add("endika.fernandezcuesta@gmail.com");
//		SendEmails.SendEmail(to, "Asunto", body.toString());
	}

}
