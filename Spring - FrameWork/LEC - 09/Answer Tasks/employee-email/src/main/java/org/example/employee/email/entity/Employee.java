package org.example.employee.email.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_employee_seq")
    @SequenceGenerator(name = "task_employee_seq", sequenceName = "task_employee_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Email> emails = new ArrayList<>();

    public void addEmail(Email email) {
        emails.add(email);
        email.setEmployee(this);
    }
}
