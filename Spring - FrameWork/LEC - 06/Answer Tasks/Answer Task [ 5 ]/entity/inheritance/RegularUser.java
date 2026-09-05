package entity.inheritance;

import entity.User;

import javax.persistence.*;

@Entity
@DiscriminatorValue("REGULAR")
public class RegularUser extends User {

    @Column(name = "SUBSCRIPTION_TYPE")
    private String subscriptionType;

    public RegularUser() {
        super();
    }

    public RegularUser(String name, int age, String subscriptionType) {
        super(name, age);
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}