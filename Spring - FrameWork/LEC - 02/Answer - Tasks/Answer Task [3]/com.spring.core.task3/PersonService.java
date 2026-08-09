package com.spring.core.task3;

public class PersonService implements UserService {

    public PersonService() {
        System.out.println("PersonService -> Constructor called");
    }

    @Override
    public void save(String name) {
        System.out.println("PersonService -> Saving person: " + name);
    }

    public void init() {
        System.out.println("PersonService -> init() called");
    }

    public void destroy() {
        System.out.println("PersonService -> destroy() called");
    }
}