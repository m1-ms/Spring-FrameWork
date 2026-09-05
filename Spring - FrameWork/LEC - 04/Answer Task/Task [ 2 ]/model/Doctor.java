package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "DOCTOR")
public class Doctor {

    // id - user name - salary

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "doctor_seq")
    @SequenceGenerator(name = "doctor_seq" , sequenceName = "DOCTOR_SEQ" , allocationSize = 1)
    private int id;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "SALARY")
    private double salary;

    @ManyToOne
    @JoinColumn(name = "HOSPITAL_ID")
    private Hospital hospital;

    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;

    public Doctor() {

    }

    public Doctor(String userName, double salary) {
        this.userName = userName;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", salary=" + salary +
                '}';
    }

}
