package org.example;

import model.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        /*
        Player player1 = new Player("Mohamed Salah", 32, true);
        Player player2 = new Player("Ahmed Alaa", 33, true);
        // Player player3 = new Player("Mohamed Emam", 38, false); // 16


        // - When use Update in player3 so make [ Player player3 = new Player("Mohamed Emam", 38, false); ] and [ session.save(player3); ] a commit

        // Update
        Player player3_Update = session.get(Player.class, 16);
        player3_Update.setAge(40);
        player3_Update.setName("Mohamed Alei");

        session.save(player1);
        session.save(player2);
        // session.save(player3);
        */

        // session.get
        Player player_get = session.get(Player.class, 16);
        System.out.println(player_get);


        // Delete
        Player player_delete = session.get(Player.class, 14);
        session.delete(player_delete);


        transaction.commit();

        session.close();
        sessionFactory.close();

    }
}