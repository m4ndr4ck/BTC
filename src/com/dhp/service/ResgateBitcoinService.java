package com.dhp.service;

import java.util.List;

import com.dhp.model.ResgateBitcoin;

public interface ResgateBitcoinService {
	public void save(ResgateBitcoin resgateBitcoin);
	
	public List<ResgateBitcoin> findResgates(Long clienteId);
}
