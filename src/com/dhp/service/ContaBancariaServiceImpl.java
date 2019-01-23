package com.dhp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dhp.model.ContaBancaria;
import com.dhp.repository.ContaBancariaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaBancariaServiceImpl implements ContaBancariaService{
	
	@Autowired
    private ContaBancariaRepository contaBancariaRepository;
	
	@Override
	public void save(ContaBancaria contabancaria){
		contaBancariaRepository.save(contabancaria);
		
	}
	
	public void delete(long clienteid){
		for(ContaBancaria  conta : contaBancariaRepository.findByClienteid(clienteid)){
			contaBancariaRepository.delete(conta);
		}
	}
	
	@Override
	public List<ContaBancaria> findByClienteid(Long clienteid) {
		List<ContaBancaria> contasbancarias = new ArrayList<>();
		
		for(ContaBancaria  conta : contaBancariaRepository.findByClienteid(clienteid)){
			contasbancarias.add(conta);
		}
		return contasbancarias;
				
	}
	

}
