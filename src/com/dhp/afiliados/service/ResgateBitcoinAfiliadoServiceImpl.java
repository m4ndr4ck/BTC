package com.dhp.afiliados.service;

import com.dhp.afiliados.model.ResgateBitcoinAfiliado;
import com.dhp.afiliados.repository.ResgateBitcoinAfiliadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResgateBitcoinAfiliadoServiceImpl implements ResgateBitcoinAfiliadoService {

    @Autowired
    private ResgateBitcoinAfiliadoRepository resgateBitcoinAfiliadoRepository;

    public void save(ResgateBitcoinAfiliado resgateBitcoinAfiliado){
        resgateBitcoinAfiliadoRepository.save(resgateBitcoinAfiliado);
    }

    public List<ResgateBitcoinAfiliado> findResgates(Long afiliadoid){
        return resgateBitcoinAfiliadoRepository.findByAfiliadoid(afiliadoid);
    }

}