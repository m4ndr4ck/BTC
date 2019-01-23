package com.dhp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;


@Entity
@Table(name = "pagamentos")
public class Pagamentos {
	
	@Column
	private String cotacao;
	
	@Column
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime dataHora;
	
	@Column(precision=16, scale=8)
	private double bitcoins;

	@Column(precision=16, scale=8)
	private double taxa;

	@Column(precision=13, scale=2)
	private double valorPagamento;
	
	@Column
	private int tipoPagamento;
	
	@Column
	private long clienteid;
	
	@Column
	private long status;
	
	@Column
	private String pagseguroid;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	@Transient
	String nome;
	
	@Transient
	String valorPagamentoComTaxa;
	
	public String getValorPagamentoComTaxa() {
		return valorPagamentoComTaxa;
	}

	public void setValorPagamentoComTaxa(String valorPagamentoComTaxa) {
		this.valorPagamentoComTaxa = valorPagamentoComTaxa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Transient
	String erros;

	public String getErros() {
		return erros;
	}

	public void setErros(String erros) {
		this.erros = erros;
	}

	public String getCotacao() {
		return cotacao;
	}
	
	public void setCotacao(String cotacao) {
		this.cotacao = cotacao;
	}
	
	public LocalDateTime getDataHora() {
		return dataHora;
	}
	
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	
	public double getBitcoins() {
		return bitcoins;
	}
	
	public void setBitcoins(double bitcoins) {
		this.bitcoins = bitcoins;
	}
	
	public double getValorPagamento() {
		return valorPagamento;
	}
	
	public void setValorPagamento(double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public long getClienteid() {
		return clienteid;
	}

	public void setClienteid(long clienteid) {
		this.clienteid = clienteid;
	}

	public int getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(int tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
	
	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPagseguroid() {
		return pagseguroid;
	}

	public void setPagseguroid(String pagseguroid) {
		this.pagseguroid = pagseguroid;
	}

	public double getTaxa() {
		return taxa;
	}

	public void setTaxa(double taxa) {
		this.taxa = taxa;
	}


}
