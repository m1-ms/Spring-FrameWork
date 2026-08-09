package com.spring.core.task1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

    	
        // Container 1 : XML Container
        System.out.println(" --- XML Container --- ");
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("AppContextTask1.xml");

        UserService personXml = (UserService) xmlContext.getBean("personService");
        UserService managerXml = (UserService) xmlContext.getBean("managerService");

        personXml.save("Ahmed");
        personXml.update("Omar");
        managerXml.save("Mona");
        managerXml.update("Aya");
        
        System.out.println(" ");
        System.out.println(" ------------------------------------------------------------ ");
        System.out.println(" ");
        
        // Container 2 : Java Container ( No XML)
        System.out.println(" --- Java Container --- ");
        ApplicationContext javaContext = new AnnotationConfigApplicationContext(StartApp.class);

        UserService personJava = (UserService) javaContext.getBean("personService");
        UserService managerJava = (UserService) javaContext.getBean("managerService");

        personJava.save("Ahmed");
        personJava.update("Omar");
        managerJava.save("Mona");
        managerJava.update("Aya");
        
    }
}