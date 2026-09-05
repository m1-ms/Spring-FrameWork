package org.example;

import entity.Friends;
import entity.Post;
import entity.User;
import entity.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration().addAnnotatedClass(User.class).addAnnotatedClass(UserDetails.class)
                .addAnnotatedClass(Friends.class).addAnnotatedClass(Post.class)
                .configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        /*

        // One-to-One > User + UserDetails
        UserDetails details1 = new UserDetails("Cairo, Egypt", "0100000000");
        User user1 = new User("Ahmed", 25);
        user1.setUserDetails(details1);

        // Many-to-Many > User + Friends
        Friends friend1 = new Friends("Omar");
        Friends friend2 = new Friends("Sara");
        user1.getFriends().add(friend1);
        user1.getFriends().add(friend2);

        // One-to-Many > User + Post
        Post post1 = new Post("First Post", "This is my first post content");
        Post post2 = new Post("Second Post", "Another post content");
        post1.setUser(user1);
        post2.setUser(user1);

        session.persist(details1);
        session.persist(friend1);
        session.persist(friend2);
        session.persist(user1);
        session.persist(post1);
        session.persist(post2);

        */

        /*

        // Add User with UserDetails
        UserDetails details = new UserDetails("Alexandria, Egypt", "0111111111");
        User userWithDetails = new User("Mostafa", 22);
        userWithDetails.setUserDetails(details);

        session.persist(details);
        session.persist(userWithDetails);


        // Add User with Friends
        Friends friendA = new Friends("Youssef");
        Friends friendB = new Friends("Nour");
        User userWithFriends = new User("Hana", 24);
        userWithFriends.getFriends().add(friendA);
        userWithFriends.getFriends().add(friendB);

        session.persist(friendA);
        session.persist(friendB);
        session.persist(userWithFriends);


        // Add User with Post
        User userWithPost = new User("Karim", 30);
        Post post = new Post("My Journey", "This is the content of the post");
        post.setUser(userWithPost);

        session.persist(userWithPost);
        session.persist(post);

        */

        // Add User with UserDetails (Cascade)

        UserDetails details = new UserDetails("Giza, Egypt", "0122222222");
        User userWithDetails = new User("Tarek", 27);
        userWithDetails.setUserDetails(details);

        session.persist(userWithDetails);

        // Add User with Friends (Cascade)

        Friends friendC = new Friends("Sherif");
        Friends friendD = new Friends("Dina");
        User userWithFriends = new User("Laila", 26);
        userWithFriends.getFriends().add(friendC);
        userWithFriends.getFriends().add(friendD);

        session.persist(userWithFriends);

        // Add User with Post (Cascade)

        User userWithPost = new User("Sameh", 29);
        Post post = new Post("New Beginnings", "Excited to start this journey");
        post.setUser(userWithPost);

        session.persist(post);



        transaction.commit();

        session.close();
        sessionFactory.close();
    }
}