package com.spring.core.task1;

public class ManagerService implements UserService {

	@Override
	public void save(String name) {
        System.out.println("ManagerService > Saving Manager : " + name);
		
	}

	@Override
	public void update(String name) {
        System.out.println("ManagerService > Updating Manager : " + name);
		
	}

}
