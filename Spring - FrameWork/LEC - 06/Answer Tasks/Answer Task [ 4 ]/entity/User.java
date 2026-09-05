package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    // id - name - age

    // @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    // @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "id")
    // private UserDetails userDetails;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "id")
    private UserDetails userDetails;

    /*

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "id")
    private UserDetails userDetails;

     */

    @ManyToMany (cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "USER_FRIENDS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FRIEND_ID")
    )
    private List<Friends> friends = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE" , columnDefinition = "NUMBER CHECK (AGE > 0)")
    private int age;

    /*

    @OneToOne
    @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "id")
    private UserDetails userDetails;

    @ManyToMany
    @JoinTable(
            name = "USER_FRIENDS",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FRIEND_ID")
    )
    private List<Friends> friends = new ArrayList<>();

     */

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public List<Friends> getFriends() {
        return friends;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
