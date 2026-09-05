package model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity // change class to table database
@Table (name = "PLAYER") // it's name table name " PLAYER " -> [option]

public class Player {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE , generator = "player_seq")
    @SequenceGenerator (name = "player_seq" , sequenceName = "PLAYER_SEQ" , allocationSize = 1)
    private int id;

    @Column (name = "NAME" , nullable = false)
    private String name;

    @Column (name = "AGE" , length = 10)
    private int age;

    @Column (name = "STATUS")
    private boolean status;

    public Player () {

    }

    public Player(String name, int age, boolean status) {
        this.name = name;
        this.age = age;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", status=" + status +
                '}';
    }
}
