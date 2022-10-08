package org.model;

import java.util.List;

public interface IEstadosDAO {
    void Insert(Estados estados);
    void Update(Estados estados);
    void Delete(long idEstado);
    List<Estados> Read();
    Estados Select(long idEstado);
    List<Estados> SelectByUF(String uf);
}
