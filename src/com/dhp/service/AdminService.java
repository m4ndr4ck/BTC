package com.dhp.service;

import java.util.List;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.model.Pagamentos;
import com.dhp.model.ResgateBitcoin;
import com.dhp.model.ResgateContaBancaria;
import com.dhp.model.User;

public interface AdminService {
	
	public List<User> retrieveAllCustomers();
	
	public List<Pagamentos> retrieveAllCompras();
	
	public List<ResgateBitcoin> retrieveAllResgateBitcoin();
	
	public List<ResgateContaBancaria> retrieveAllResgateContaBancaria();

	}
