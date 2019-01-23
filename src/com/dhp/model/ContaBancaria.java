package com.dhp.model;

import javax.persistence.*;

@Entity
@Table(name = "contabancaria")
public class ContaBancaria {
	private Long clienteid;
	private Long id;
	private String banco;
	private String tipodeconta;
	private String agencia;
	private String conta;
	private String dadosadicionais;
	
	public Long getClienteid() {
		return clienteid;
	}
	
	public void setClienteid(Long clienteid) {
		this.clienteid = clienteid;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
