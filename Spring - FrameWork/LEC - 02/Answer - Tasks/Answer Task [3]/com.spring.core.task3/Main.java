package com.spring.core.task3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        System.out.println(" --- XML Container --- ");
        ClassPathXmlApplicationContext xmlContext = new ClassPathXmlApplicationContext("AppContextTask3.xml");

        UserService p1 = (UserService) xmlContext.getBean("personService");
        UserService p2 = (UserService) xmlContext.getBean("personService");

        System.out.println("Same instance? " + (p1 == p2));
        p1.save("Ahmed");
        xmlContext.close();

        System.out.println(" ");
        System.out.println(" ------------------------------------------------------------ ");
        System.out.println(" ");
        
        System.out.println(" --- Java Container --- ");
        AnnotationConfigApplicationContext javaContext = new AnnotationConfigApplicationContext(StartApp.class);

        UserService p3 = (UserService) javaContext.getBean("personService");
        p3.save("Mona");
        javaContext.close();
    }
}