package com.spring.core.task2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        // Container 1: XML Container
        System.out.println(" --- XML Container --- ");
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("AppContextTask2.xml");

        AccountService accountXml = (AccountService) xmlContext.getBean("accountService");
        accountXml.getSavePerson("Ahmed");

        System.out.println(" ");
        System.out.println(" ------------------------------------------------------------ ");
        System.out.println(" ");
        
        // Container 2: Java
        
        System.out.println(" --- Java Container --- ");
        ApplicationContext javaContext = new AnnotationConfigApplicationContext(StartApp.class);

        AccountService accountJava = (AccountService) javaContext.getBean("accountService");
        accountJava.getSavePerson("Mona");
    }
}