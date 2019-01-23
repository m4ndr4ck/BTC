package com.dhp.repository;

import com.dhp.model.ResgateContaBancaria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResgateContaBancariaRepository extends JpaRepository<ResgateContaBancaria, Long>{
	List<ResgateContaBancaria> findByClienteid(Long clienteid);
}