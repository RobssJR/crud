package org.model;

import java.util.List;

public interface IEstadosDAO {
    void Insert(Estados estados);
    void Update(Estados estados);
    void Delete(long idEstado);
    List<Estados> Read();
}
