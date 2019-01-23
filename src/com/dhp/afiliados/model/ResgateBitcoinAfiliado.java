package com.dhp.afiliados.model;

import com.dhp.model.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "afl_saque")
public class ResgateBitcoinAfiliado {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(precision=16, scale=8)
    private double bitcoins;
    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dataHora;
    @Column
    private String enderecobitcoin;
    @Column
    private Long afiliadoid;

    @Transient
    String erros;

    @Transient
    String nome;

    @Transient
    String totalresgatado;

    public String getTotalresgatado() {
        return totalresgatado;
    }

    public void setTotalresgatado(String totalresgatado) {
        this.totalresgatado = totalresgatado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getErros() {
        return erros;
    }

    public void setErros(String erros) {
        this.erros = erros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBitcoins() {
        return bitcoins;
    }

    public void setBitcoins(double bitcoins) {
        this.bitcoins = bitcoins;
    }

    public String getEnderecobitcoin() {
        return enderecobitcoin;
    }

    public void setEnderecobitcoin(String enderecobitcoin) {
        this.enderecobitcoin = enderecobitcoin;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getAfiliadoid() {
        return afiliadoid;
    }

    public void setAfiliadoid(Long afiliadoid) {
        this.afiliadoid = afiliadoid;
    }

}
