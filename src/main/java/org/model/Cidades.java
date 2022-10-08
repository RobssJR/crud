package org.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cidade")
public class Cidades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long idCidade;
    @ManyToOne
    @JoinColumn(name = "idEstado")
    public Estados estados;
    @Column(length = 50)
    public String nome;
    @Override
    public String toString() {
        return nome;
    }
}
