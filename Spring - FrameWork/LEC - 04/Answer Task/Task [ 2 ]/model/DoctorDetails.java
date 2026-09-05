package model;

import javax.persistence.*;

@Entity
@Table(name = "DOCTOR_DETAILS")
public class DoctorDetails {

    // id - fullAddress - firstName - lastName - age

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_details_seq")
    @SequenceGenerator(name = "doctor_details_seq", sequenceName = "DOCTOR_DETAILS_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "FULL_ADDRESS")
    private String fullAddress;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "AGE")
    private int age;

    @OneToOne       // Relation [ One To One ]
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    public DoctorDetails() {

    }

    public DoctorDetails(String fullAddress, String firstName, String lastName, int age) {
        this.fullAddress = fullAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

}
