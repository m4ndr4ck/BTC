package com.dhp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dhp.model.ResgateBitcoin;
import com.dhp.repository.ResgateBitcoinRepository;
import org.springframework.stereotype.Service;

@Service
public class ResgateBitcoinServiceImpl implements ResgateBitcoinService {
	
	@Autowired
	private ResgateBitcoinRepository resgateBitcoinRepository;
	
    public void save(ResgateBitcoin resgateBitcoin){
    	resgateBitcoinRepository.save(resgateBitcoin);
    }
    
    public List<ResgateBitcoin> findResgates(Long clienteId){
    	return resgateBitcoinRepository.findByClienteid(clienteId);
    }

}
