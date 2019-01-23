package com.dhp.afiliados.service;

import com.dhp.afiliados.model.ResgateBitcoinAfiliado;

import java.util.List;

public interface ResgateBitcoinAfiliadoService {

    public void save(ResgateBitcoinAfiliado resgateBitcoinAfiliado);

    public List<ResgateBitcoinAfiliado> findResgates(Long afiliado);
}