package com.dhp.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.dhp.model.EnderecosBTC;
import com.dhp.repository.EnderecosBTCRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecosBTCServiceImpl implements EnderecosBTCService {
    
	@Autowired
    private EnderecosBTCRepository enderecosBTCRepository;
	
    
    public void save(Long clienteid, String endereco, String uuid){
    	EnderecosBTC enderecoBTC = new EnderecosBTC();
    	enderecoBTC.setClienteid(clienteid);
    	enderecoBTC.setEndereco(endereco);
    	enderecosBTCRepository.save(enderecoBTC);
    }

	public void save(int clienteid, String endereco){
		EnderecosBTC enderecoBTC = new EnderecosBTC();
		enderecoBTC.setClienteid((long)clienteid);
		enderecoBTC.setEndereco(endereco);
		enderecosBTCRepository.save(enderecoBTC);
	}

	@Override
	public EnderecosBTC findByClienteid(Long clienteid){
		return enderecosBTCRepository.findByClienteid(clienteid);
    }

	@Override
	public List<EnderecosBTC> findAll(){
		return enderecosBTCRepository.findAll();
	}


}
