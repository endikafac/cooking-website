package com.cookingwebsite.crud;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

@OpenAPIDefinition(info = @Info(title = "CookingWebSite API Rest ", version = "0.1.0", description = "API Rest service for Cooking Web Site application"))
@SecurityScheme(name = "CookingWebSiteapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@SpringBootApplication
@Slf4j
@CrossOrigin(origins = "*")
public class CookingWebSiteApplication extends SpringBootServletInitializer {
	
	public static void main(final String[] args) {
		
		log.info("Starting to run the CookingWebSite application...");
		SpringApplication application = new SpringApplication(CookingWebSiteApplication.class);
		application.setAdditionalProfiles("ssl");
		// SpringApplication.run(CookingWebSiteApplication.class, args);
		application.run(args);
		log.info("CookingWebSite application is up correctly.");

	}
	
	@Bean
	public ServletWebServerFactory servletContainer() {
		// Enable SSL Trafic
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		
		// Add HTTP to HTTPS redirect
		tomcat.addAdditionalTomcatConnectors(this.httpToHttpsRedirectConnector());
		
		return tomcat;
	}
	
	/*
	 * We need to redirect from HTTP to HTTPS. Without SSL, this application used port 8082. With SSL it will use port 8443.
	 * So, any request for 8082 needs to be redirected to HTTPS on 8443.
	 */
	private Connector httpToHttpsRedirectConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(8443);
		return connector;
	}

}
