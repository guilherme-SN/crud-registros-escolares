package br.com.guilherme.regescweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class RegescwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegescwebApplication.class, args);
	}

}
