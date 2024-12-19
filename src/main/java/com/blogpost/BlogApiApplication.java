package com.blogpost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Blog Post API",
				version = "1.0",
				description = "API for managing blog posts and comments"))
@EnableAsync
public class BlogApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
	}
}
