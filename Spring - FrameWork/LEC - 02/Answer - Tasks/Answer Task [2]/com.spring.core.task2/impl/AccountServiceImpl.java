package com.spring.core.task2.impl;

import com.spring.core.task2.AccountService;
import com.spring.core.task2.UserService;

public class AccountServiceImpl implements AccountService {

    private UserService personService;

    public AccountServiceImpl(UserService personService) {
        this.personService = personService;
    }

    @Override
    public void getSavePerson(String name) {
        personService.save(name);
    }
    
}