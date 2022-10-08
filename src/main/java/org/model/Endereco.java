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
    @ManyToOne()
    @JoinColumn(name = "idCidade")
    public Cidades cidade;
    @Column(length = 9, unique = true)
    public String CEP;
    @Column(length = 50)
    public String complemento;
    @Column()
    public int numeroCasa;

}
