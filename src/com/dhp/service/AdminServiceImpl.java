package com.dhp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhp.model.Pagamentos;
import com.dhp.model.ResgateBitcoin;
import com.dhp.model.ResgateContaBancaria;
import com.dhp.model.User;
import com.dhp.repository.PagamentosRepository;
import com.dhp.repository.ResgateBitcoinRepository;
import com.dhp.repository.ResgateContaBancariaRepository;
import com.dhp.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private PagamentosRepository pagamentosRepository;
    
    @Autowired
    private ResgateBitcoinRepository resgateBitcoinRepository;
    
    @Autowired
    private ResgateContaBancariaRepository resgateContaBancariaRepository;
    
    public List<User> retrieveAllCustomers(){
    	return userRepository.findAll();
    }
    
    public List<Pagamentos> retrieveAllCompras(){
    	return pagamentosRepository.findAll();
    }
    
    public List<ResgateBitcoin> retrieveAllResgateBitcoin(){
    	return resgateBitcoinRepository.findAll();
    }
    
    public List<ResgateContaBancaria> retrieveAllResgateContaBancaria(){
    	return resgateContaBancariaRepository.findAll();
    }

}
