package com.unosquare.carmigo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Gateway to start CarMigo application.
 */
@SpringBootApplication
@EnableFeignClients
public class CarMigoApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarMigoApplication.class, args);
  }
}
