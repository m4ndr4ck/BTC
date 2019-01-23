package com.dhp.afiliados.repository;

import com.dhp.afiliados.model.Afiliado;
import com.dhp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AfiliadoRepository extends JpaRepository<Afiliado, Long> {

    Afiliado findByEmail(String email);
    Afiliado findById(long id);

    @Override
    List<Afiliado> findAll();

    Afiliado findByRefEndingWith(String refFinal);

}
