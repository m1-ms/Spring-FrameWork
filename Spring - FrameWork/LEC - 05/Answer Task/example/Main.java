package org.example;

import entity.Course;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration().addAnnotatedClass(Student.class)
                .addAnnotatedClass(Course.class)
                .configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

/*
        /// Courses
        Course course1 = new Course("Math");
        Course course2 = new Course("Physics");
        Course course3 = new Course("Chemistry");
        Course course4 = new Course("Biology");
        Course course5 = new Course("English");

        /// Students
        Student student1 = new Student("Ahmed");
        Student student2 = new Student("Sara");
        Student student3 = new Student("Mai");

        /// getCourses for add
        student1.getCourses().add(course1);
        student1.getCourses().add(course2);
        student1.getCourses().add(course3);

        student2.getCourses().add(course2);
        student2.getCourses().add(course4);

        student3.getCourses().add(course3);
        student3.getCourses().add(course4);
        student3.getCourses().add(course5);

        /// Save the Courses
        session.persist(course1);
        session.persist(course2);
        session.persist(course3);
        session.persist(course4);
        session.persist(course5);

        /// Save the Students
        session.persist(student1);
        session.persist(student2);
        session.persist(student3);
*/
/*
        // Fetch Student 1
        Student fetchedStudent = session.get(Student.class, 1);
        System.out.println("Student : " + fetchedStudent.getName());

        for (Course course : fetchedStudent.getCourses()) {
            System.out.println("  - Course: " + course.getTitle());
        }

        System.out.println("------------------------");

        // Fetch Student 3
        Course fetchedCourse = session.get(Course.class, 3);
        System.out.println("Course : " + fetchedCourse.getTitle());

        for (Student student : fetchedCourse.getStudents()) {
            System.out.println("  - Student : " + student.getName());
        }
*/

        // get student 1
        Student existingStudent = session.get(Student.class, 1);

        // Create new course
        Course newCourse = new Course("Art");

        existingStudent.getCourses().add(newCourse);
        session.persist(newCourse);


        transaction.commit();

        session.close();
        sessionFactory.close();
    }
}