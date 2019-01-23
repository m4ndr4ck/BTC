package com.dhp.afiliados.model;

import javax.persistence.*;

@Entity
@Table(name = "afl_enderecos_btc")
public class EnderecosBTCAfiliado {
    private Long afiliadoid;
    private Long btcid;
    private String endereco;

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Column(precision=16, scale=8)
    private double saldo;

    public Long getAfiliadoid() {
        return afiliadoid;
    }

    public void setAfiliadoid(Long afiliadoid) {
        this.afiliadoid = afiliadoid;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getBtcid() {
        return btcid;
    }

    public void setBtcid(Long btcid) {
        this.btcid = btcid;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


}
