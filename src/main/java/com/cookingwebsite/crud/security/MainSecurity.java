package com.cookingwebsite.crud.security;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.cookingwebsite.crud.security.jwt.JwtEntryPoint;
import com.cookingwebsite.crud.security.jwt.JwtTokenFilter;
import com.cookingwebsite.crud.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurity extends WebSecurityConfigurerAdapter {
	
	@Value("${trust.store}")
	private Resource trustStore;
	
	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	JwtEntryPoint jwtEntryPoint;

	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/auth/**").permitAll().anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint(this.jwtEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(this.jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	public RestTemplate restTemplate() throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(this.trustStore.getURL(), this.trustStorePassword.toCharArray())
				.build();
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
		HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(factory);
	}
}
