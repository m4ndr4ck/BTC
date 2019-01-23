package com.dhp.repository;

import com.dhp.model.Pagamentos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Long>{
	List<Pagamentos> findByClienteid(Long clienteid);
	Pagamentos findByPagseguroid(String pagseguroid);

}
