package org.model;

import jakarta.persistence.*;
@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long idEndereco;
    @Column(length = 50)
    public String rua;
    @Column(length = 50)
    public String bairro;
    @Column(length = 50)
    public String cidade;
    @Column(length = 2)
    public String estado;
    @Column(length = 9)
    public String CEP;
    @Column(length = 50)
    public String complemento;
    @Column(length = 2)
    public String UF;
}