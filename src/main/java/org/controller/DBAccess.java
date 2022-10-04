package org.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.text.Normalizer;
import java.util.List;

public class DBAccess {
    private static EntityManagerFactory entityManagerFactory;
    public static EntityManager entityManager;
    private static DBAccess dbAccess;

    private DBAccess() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("endereco");
            entityManager = entityManagerFactory.createEntityManager();

        } catch (Exception e) {
            entityManagerFactory.close();
            throw new RuntimeException(e);
        }
    }

    public static DBAccess getInstance() {
        if(dbAccess == null) {
            dbAccess = new DBAccess();
        }

        return dbAccess;
    }
}
