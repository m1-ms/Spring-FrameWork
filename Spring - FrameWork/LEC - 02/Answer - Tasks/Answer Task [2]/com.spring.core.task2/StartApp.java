package com.spring.core.task2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.core.task2.impl.AccountServiceImpl;


@Configuration
public class StartApp {

    @Bean
    public UserService personService() {
        return new PersonService();
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl(personService());
    }
}