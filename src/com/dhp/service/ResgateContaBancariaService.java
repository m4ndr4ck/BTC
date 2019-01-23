package com.dhp.service;

import java.util.List;
import com.dhp.model.ResgateContaBancaria;

public interface ResgateContaBancariaService {
	public void save(ResgateContaBancaria resgateContaBancaria);
	
	public List<ResgateContaBancaria> findResgates(Long clienteId);

}
