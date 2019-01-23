package com.dhp.model;

import javax.persistence.*;

@Entity
@Table(name = "enderecos_btc")
public class EnderecosBTC {
	private Long clienteid;
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

	public Long getClienteid() {
		return clienteid;
	}
	
	public void setClienteid(Long clienteid) {
		this.clienteid = clienteid;
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
