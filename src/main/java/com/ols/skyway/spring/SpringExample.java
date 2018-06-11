package com.ols.skyway.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringExample {

  public static void main(String[] args) {
    SpringApplication.run(SpringExample.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      // Simple spring boot application to demonstrate possible xml marshalling techniques
    };
  }

}
