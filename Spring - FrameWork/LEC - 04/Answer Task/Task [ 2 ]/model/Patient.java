package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PATIENT")
public class Patient {

    // id - name - type of disease

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "patient_seq")
    @SequenceGenerator(name = "patient_seq" , sequenceName = "PATIENT_SEQ" , allocationSize = 1)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE_OF_DISEASE")
    private String typeOfDisease;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    @ManyToMany(mappedBy = "patients")
    private List<Hospital> hospitals;

    public Patient() {

    }

    public Patient(String name, String typeOfDisease) {
        this.name = name;
        this.typeOfDisease = typeOfDisease;
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

    public String getTypeOfDisease() {
        return typeOfDisease;
    }

    public void setTypeOfDisease(String typeOfDisease) {
        this.typeOfDisease = typeOfDisease;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeOfDisease='" + typeOfDisease + '\'' +
                '}';
    }

}
