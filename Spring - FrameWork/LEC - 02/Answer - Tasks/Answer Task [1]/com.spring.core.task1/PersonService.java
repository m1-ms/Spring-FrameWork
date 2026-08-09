package com.spring.core.task1;

public class PersonService implements UserService {

	@Override
	public void save(String name) {
        System.out.println("PersonService > Saving Person : " + name);
		
	}

	@Override
	public void update(String name) {
        System.out.println("PersonService > Updating Person : " + name);
		
	}

}
