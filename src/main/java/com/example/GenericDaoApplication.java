package com.example;

import com.example.dao.UserDao;
import com.example.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class GenericDaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenericDaoApplication.class, args);
    }

    @Bean
    public CommandLineRunner clr(UserDao userDao) {
        return args -> {
            User user1 = new User();
            user1.setName("Jane");
            user1.setSurname("Doe");

            User user2 = new User();
            user2.setName("Jane");
            user2.setSurname("Smith");

            userDao.saveAll(Arrays.asList(user1, user2));

            System.out.println("#######################################");

            userDao.findByField("name", "Jane")
                    .forEach(System.out::println);
        };
    }

}
