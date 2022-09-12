package org.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.Cidades;
import org.model.Endereco;
import org.model.Estados;

import java.util.List;

public class Controller {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("endereco");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static void CadastroEndereco(Endereco endereco) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(endereco);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Endereco> getEnderecos() {
        return null;
    }

    public static List<Cidades> getAllCidades() {
        return entityManager.createQuery("from Cidades").getResultList();
    }

    public static List<Estados> getAllEstados() {
        return entityManager.createQuery("from Estados").getResultList();
    }

    public static List<Endereco> getAllEnderecos() {
        return entityManager.createQuery("from Endereco").getResultList();
    }

    public static void deleteEndereco(long idEndereco) {
        try {
            entityManager.getTransaction().begin();
            Endereco endereco = entityManager.find(Endereco.class,idEndereco);
            entityManager.remove(endereco);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
