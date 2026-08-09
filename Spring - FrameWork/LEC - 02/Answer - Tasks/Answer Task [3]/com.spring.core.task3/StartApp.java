package com.spring.core.task3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Configuration
public class StartApp {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserService personService() {
        return new PersonService();
    }
}