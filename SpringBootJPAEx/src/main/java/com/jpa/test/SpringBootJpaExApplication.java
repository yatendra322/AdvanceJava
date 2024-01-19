package com.jpa.test;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jpa.test.dao.UserRepository;
import com.jpa.test.entities.User;

import lombok.Data;

@SpringBootApplication
@Data
public class SpringBootJpaExApplication {

	public static void main(String[] args) {
		ApplicationContext contex = SpringApplication.run(SpringBootJpaExApplication.class, args);
		UserRepository userRepository = contex.getBean(UserRepository.class);

		List<User> result = userRepository.findByName("Praveen");

		result.forEach(u -> {
			System.out.println("-----------> \n "+u+"\n\n");
		});
	}
}
