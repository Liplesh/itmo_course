package ru.lipnin.itmohomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EnableFeignClients
@SpringBootApplication
public class ItmohomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItmohomeworkApplication.class, args);
	}

}
