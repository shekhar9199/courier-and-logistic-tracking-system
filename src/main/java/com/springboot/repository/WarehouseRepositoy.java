package com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Warehouse;

public interface WarehouseRepositoy extends JpaRepository<Warehouse, Integer> {
	
	boolean existsByContact(String contact);
	
	List<Warehouse> findByLocation(String location);
	
	boolean existsByContactAndIdNot(String contact, Integer id);
	
	
}
