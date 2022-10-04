package org.model;

import java.util.List;

public interface IEnderecoDAO {
    void Insert(Endereco endereco);
    void Update(Endereco endereco);
    void Delete(long idEndereco);
    List<Endereco> Read();
}