package com.dhp.service;

import java.util.List;

import com.dhp.model.ContaBancaria;

public interface ContaBancariaService {
	void save(ContaBancaria contabancaria);
	
	void delete(long clienteid);
	
	List<ContaBancaria> findByClienteid(Long clienteid);

}
