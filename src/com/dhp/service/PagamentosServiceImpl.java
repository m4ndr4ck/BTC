package com.dhp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhp.model.Pagamentos;
import com.dhp.repository.PagamentosRepository;

@Service
public class PagamentosServiceImpl implements PagamentosService {
	
	@Autowired
    private PagamentosRepository pagamentosRepository;
	
	@Override
    public void save(Pagamentos pagamento) {
        pagamentosRepository.saveAndFlush(pagamento);
    }
    
    @Override
    public List<Pagamentos> findPagamentos(long clienteId){
    	return pagamentosRepository.findByClienteid(clienteId);
    }

    @Override
    public Pagamentos findByPagseguroid(String pagseguroid){
        return pagamentosRepository.findByPagseguroid(pagseguroid);
    };

}
