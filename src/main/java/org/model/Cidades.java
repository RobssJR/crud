package org.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cidade")
public class Cidades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCidade;
    @ManyToOne
    @JoinColumn(name = "idEstado")
    private Estados estados;
    @Column(length = 50)
    private String nome;

    public long getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(long idCidade) {
        this.idCidade = idCidade;
    }

    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
