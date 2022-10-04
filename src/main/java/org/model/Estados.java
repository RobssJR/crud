package org.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estados")
public class Estados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long idEstado;
    @Column(length = 10)
    public String uf;
    @Column(length = 20)
    public String nome;

    @Override
    public String toString() {
        return uf;
    }
}
