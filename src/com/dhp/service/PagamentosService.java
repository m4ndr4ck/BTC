package com.dhp.service;


import java.util.List;

import com.dhp.model.Pagamentos;

public interface PagamentosService {
    void save(Pagamentos pagamento);
    
    List<Pagamentos> findPagamentos(long clienteId);

    Pagamentos findByPagseguroid(String pagseguroid);
}