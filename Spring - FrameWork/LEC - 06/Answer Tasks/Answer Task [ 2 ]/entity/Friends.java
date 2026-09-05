package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FRIENDS")
public class Friends {

    // id - name

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friends_seq")
    @SequenceGenerator(name = "friends_seq", sequenceName = "FRIENDS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(mappedBy = "friends")
    private List<User> users = new ArrayList<>();

    public Friends() {
    }

    public Friends(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
