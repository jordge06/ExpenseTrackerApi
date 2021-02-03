package com.example.authwithdatabase;

import com.example.authwithdatabase.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthWithDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthWithDatabaseApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/categories/*");
		return registrationBean;
	}

}
