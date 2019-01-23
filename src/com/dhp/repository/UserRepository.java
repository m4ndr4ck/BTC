package com.dhp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCpf(String cpf);
    User findById(long id);
    List<User> findByRef(String ref);
    User findByRefEndingWith(String refFinal);
    	
    @Override
    List<User> findAll();
}
