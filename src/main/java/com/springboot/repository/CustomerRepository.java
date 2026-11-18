package com.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.entity.Customer;

public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);

    boolean existsByContact(String contact);
    
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByContactAndIdNot(String contact, Integer id);
    
    Optional<Customer> findByContact(String contact);
}