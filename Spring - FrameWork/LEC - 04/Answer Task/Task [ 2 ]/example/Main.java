package org.example;

import model.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Hospital hospital = new Hospital("Al Salam Hospital", 0, 0);
        session.save(hospital);

        Doctor doctor = new Doctor("dr_mohamed", 15000.0);
        doctor.setHospital(hospital);
        session.save(doctor);

        DoctorDetails doctorDetails = new DoctorDetails("123 Nile St, Cairo", "Mohamed", "Ali", 35);
        doctorDetails.setDoctor(doctor);
        session.save(doctorDetails);

        Patient patient1 = new Patient("Ali Hassan", "Diabetes");
        patient1.setDoctor(doctor);
        session.save(patient1);

        Patient patient2 = new Patient("Sara Ahmed", "Asthma");
        patient2.setDoctor(doctor);
        session.save(patient2);

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);
        hospital.setPatients(patientList);

        transaction.commit();

        session.close();
        sessionFactory.close();

    }
}