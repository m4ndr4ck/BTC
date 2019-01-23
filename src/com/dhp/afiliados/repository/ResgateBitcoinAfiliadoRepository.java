package com.dhp.afiliados.repository;

import com.dhp.afiliados.model.ResgateBitcoinAfiliado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResgateBitcoinAfiliadoRepository extends JpaRepository<ResgateBitcoinAfiliado, Long> {
    List<ResgateBitcoinAfiliado> findByAfiliadoid(Long afiliadoid);
}
