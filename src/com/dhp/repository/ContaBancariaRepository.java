package com.dhp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhp.model.ContaBancaria;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long>{
	List<ContaBancaria> findByClienteid(Long clienteid);
}

