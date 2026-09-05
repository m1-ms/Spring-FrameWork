package entity.inheritance;

import entity.User;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends User {

    @Column(name = "ACCESS_LEVEL")
    private String accessLevel;

    public AdminUser() {
        super();
    }

    public AdminUser(String name, int age, String accessLevel) {
        super(name, age);
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
}
