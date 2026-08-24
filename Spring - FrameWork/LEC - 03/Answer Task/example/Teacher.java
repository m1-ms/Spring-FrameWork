package org.example;

import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity // Say to hibernate this class is different and represents the database table
@Check(constraints = "Age between 15 and 20") // adding a condition Check constraint at the Oracle table Level
public class Teacher {

    @Id // this field defines the primary key
    private int id;

    @Column(length = 50) // say to hibernate make name field VARCHAR2(50)
    private String name;

    private int age;

    @Column(unique = true) // say to hibernate when you make table use unique address field
    private String address;

    public Teacher(){

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
