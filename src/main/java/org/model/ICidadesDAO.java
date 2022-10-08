package org.model;

import java.util.List;

public interface ICidadesDAO {
    void Insert(Cidades cidades);
    void Update(Cidades cidades);
    void Delete(long idCidades);
    List<Cidades> Read();
    List<Cidades> SelectByName(String name);
    Cidades Select(long idCidade);
    List<Cidades> SelectByUF(String uf);
}
