package org.controller;

import org.model.Endereco;
import org.model.Estados;
import org.model.IEstadosDAO;

import java.util.List;

import static org.controller.DBAccess.entityManager;

public class EstadosDAO implements IEstadosDAO {
    @Override
    public void Insert(Estados estados) {

    }

    @Override
    public void Update(Estados estados) {

    }

    @Override
    public void Delete(long idEstado) {

    }

    @Override
    public List<Estados> Read() {
        return entityManager.createQuery("from Estados").getResultList();
    }

    @Override
    public Estados Select(long idEstado) {
        return entityManager.find(Estados.class, idEstado);
    }

    public List<Estados> SelectByUF(String uf) {
        return entityManager.createQuery("FROM Estados e WHERE e.uf = :uf", Estados.class)
                .setParameter("uf",uf)
                .getResultList();
    }
}
