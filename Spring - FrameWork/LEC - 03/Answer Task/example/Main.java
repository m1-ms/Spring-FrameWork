package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        // create teacher obj to make test
        Teacher teacher1 =  new Teacher();
        teacher1.setId(111);
        teacher1.setName(" Mahmoud ");
        teacher1.setAge(18);
        teacher1.setAddress("5-Street ,NasCity");

        Teacher teacher2 =  new Teacher();
        teacher2.setId(222);
        teacher2.setName(" Mariam ");
        teacher2.setAge(16);
        teacher2.setAddress("10-Street , Giza");

        session.save(teacher1);
        session.save(teacher2);
        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("Teacher saved Successfully!");

    }

}