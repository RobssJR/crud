package org.model;

import jakarta.persistence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.showMessageDialog;

@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEndereco;
    @Column(length = 50)
    private String rua;
    @Column(length = 50)
    private String bairro;
    @ManyToOne()
    @JoinColumn(name = "idCidade")
    private Cidades cidade;
    @Column(length = 9, unique = true)
    private String CEP;
    @Column(length = 50)
    private String complemento;
    @Column()
    private int numeroCasa;

    public long getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(long idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Cidades getCidade() {
        return cidade;
    }

    public void setCidade(Cidades cidade) {
        this.cidade = cidade;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

}
