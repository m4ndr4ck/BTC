package com.dhp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dhp.model.ResgateContaBancaria;
import com.dhp.repository.ResgateContaBancariaRepository;
import org.springframework.stereotype.Service;

@Service
public class ResgateContaBancariaServiceImpl implements ResgateContaBancariaService {
	
	@Autowired
	ResgateContaBancariaRepository resgateContaBancariaService;
	
	public void save(ResgateContaBancaria resgateContaBancaria){
		resgateContaBancariaService.save(resgateContaBancaria);
	}
	
	public List<ResgateContaBancaria> findResgates(Long clienteId){
		return resgateContaBancariaService.findByClienteid(clienteId);
	};

}
