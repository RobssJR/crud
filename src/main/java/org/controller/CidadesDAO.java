package org.controller;

import org.hibernate.query.Query;
import org.model.Cidades;
import org.model.Estados;
import org.model.ICidadesDAO;
import static org.controller.DBAccess.entityManager;

import java.text.Normalizer;
import java.util.List;

public class CidadesDAO implements ICidadesDAO {

    @Override
    public void Insert(Cidades cidades) {

    }

    @Override
    public void Update(Cidades cidades) {

    }

    @Override
    public void Delete(long idCidade) {

    }

    @Override
    public List<Cidades> Read() {
        return entityManager.createQuery("from Cidades").getResultList();
    }

    public List<Cidades> SelectByName(String name) {

        name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        List query = entityManager.createQuery("FROM Cidades c WHERE c.nome = :name", Cidades.class)
                .setParameter("name", name)
                .getResultList();

        return query;
    }

    @Override
    public Cidades Select(long idCidade) {
        return entityManager.find(Cidades.class, idCidade);
    }

    public List<Cidades> SelectByUF(String uf) {
        return entityManager.createQuery("FROM Cidades c  where c.estados.uf = :uf", Cidades.class)
                .setParameter("uf", uf)
                .getResultList();
    }
}
