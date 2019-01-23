package com.dhp.service;

import com.dhp.model.EnderecosBTC;

import java.util.List;

public interface EnderecosBTCService {
    void save(Long clienteid, String endereco, String Uuid);

    void save(int clienteid, String endereco);

    EnderecosBTC findByClienteid(Long clienteid);

    List<EnderecosBTC> findAll();
}
