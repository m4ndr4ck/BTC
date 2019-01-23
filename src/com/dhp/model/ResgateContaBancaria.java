package com.dhp.model;

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
@Table(name = "resgatecontabancaria")
public class ResgateContaBancaria {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	private Long clienteid;
	@Column
	private String banco;
	@Column
	private String tipodeconta;
	@Column
	private String agencia;
	@Column
	private String conta;
	@Column
	private String dadosadicionais;
	@Column(precision=13, scale=2)
	private String valorResgate;
	@Column
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime dataHora;
	
	@Transient
	String nome;

	public String getErros() {
		return erros;
	}

	public void setErros(String erros) {
		this.erros = erros;
	}

	@Transient
	String erros;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getClienteid() {
		return clienteid;
	}
	public void setClienteid(Long clienteid) {
		this.clienteid = clienteid;
	}
	public String getValorResgate() {
		return valorResgate;
	}
	public void setValorResgate(String valorResgate) {
		this.valorResgate = valorResgate;
	}
	public LocalDateTime getDataHora() {
		return dataHora;
	}
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getTipodeconta() {
		return tipodeconta;
	}
	public void setTipodeconta(String tipodeconta) {
		this.tipodeconta = tipodeconta;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getDadosadicionais() {
		return dadosadicionais;
	}
	public void setDadosadicionais(String dadosadicionais) {
		this.dadosadicionais = dadosadicionais;
	}


}
