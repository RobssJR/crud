package org.controller;

import org.model.Endereco;
import org.model.IEnderecoDAO;

import java.util.List;

import static org.controller.DBAccess.entityManager;

public class EnderecoDAO implements IEnderecoDAO {

    @Override
    public void Insert(Endereco endereco) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(endereco);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(Endereco endereco) {
        try {
            entityManager.getTransaction().begin();
            Endereco old = Select(endereco.getIdEndereco());

            old.setCidade(endereco.getCidade());
            old.setRua(endereco.getRua());
            old.setCEP(endereco.getCEP());
            old.setNumeroCasa(endereco.getNumeroCasa());
            old.setComplemento(endereco.getComplemento());
            old.setBairro(endereco.getBairro());

            entityManager.persist(old);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Delete(long idEndereco) {
        try {
            entityManager.getTransaction().begin();
            Endereco endereco = entityManager.find(Endereco.class,idEndereco);
            entityManager.remove(endereco);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Endereco> Read() {
        return entityManager.createQuery("from Endereco").getResultList();
    }

    @Override
    public Endereco Select(long idEndereco) {
        return entityManager.find(Endereco.class, idEndereco);
    }
}
