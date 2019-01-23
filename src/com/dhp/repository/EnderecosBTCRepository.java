package com.dhp.repository;

import com.dhp.model.EnderecosBTC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EnderecosBTCRepository extends JpaRepository<EnderecosBTC, Long>{
	EnderecosBTC findByClienteid(Long clienteid);

	@Override
	List<EnderecosBTC> findAll();
}
