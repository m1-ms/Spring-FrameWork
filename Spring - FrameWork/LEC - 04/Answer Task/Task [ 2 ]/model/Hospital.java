package model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Hospital")
public class Hospital {

    // id - name - number of doctors - number of patient

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "hospital_seq")
    @SequenceGenerator(name = "hospital_seq" , sequenceName = "HOSPITAL_SEQ" , allocationSize = 1)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NUMBER_OF_DOCTORS")
    private int numberOfDoctors;

    @Column(name = "NUMBER_OF_PATIENTS")
    private int numberOfPatient;

    @OneToMany(mappedBy = "hospital")
    private List<Doctor> doctors;

    @ManyToMany
    @JoinTable(
            name = "HOSPITAL_PATIENT",
            joinColumns = @JoinColumn(name = "HOSPITAL_ID"),
            inverseJoinColumns = @JoinColumn(name = "PATIENT_ID")
    )
    private List<Patient> patients;

    public Hospital() {

    }

    public Hospital(String name, int numberOfDoctors, int numberOfPatient) {
        this.name = name;
        this.numberOfDoctors = numberOfDoctors;
        this.numberOfPatient = numberOfPatient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfDoctors() {
        return numberOfDoctors;
    }

    public void setNumberOfDoctors(int numberOfDoctors) {
        this.numberOfDoctors = numberOfDoctors;
    }

    public int getNumberOfPatient() {
        return numberOfPatient;
    }

    public void setNumberOfPatient(int numberOfPatient) {
        this.numberOfPatient = numberOfPatient;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfDoctors=" + numberOfDoctors +
                ", numberOfPatient=" + numberOfPatient +
                '}';
    }

}
