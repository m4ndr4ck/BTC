package com.dhp.repository;

import com.dhp.model.ResgateBitcoin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResgateBitcoinRepository extends JpaRepository<ResgateBitcoin, Long>{
	List<ResgateBitcoin> findByClienteid(Long clienteid);
}