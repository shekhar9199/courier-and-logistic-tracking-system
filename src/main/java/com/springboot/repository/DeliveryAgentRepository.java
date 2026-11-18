package com.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.entity.DeliveryAgent;

public interface DeliveryAgentRepository
        extends JpaRepository<DeliveryAgent, Integer> {

    boolean existsByContact(String contact);

    boolean existsByVehicleNo(String vehicleNo);
    
    Optional<DeliveryAgent> findByVehicleNo(String vehicleNo);
    
    Optional<DeliveryAgent> findByContact(String contact);
    
    boolean existsByContactAndIdNot(String contact, Integer id);

    boolean existsByVehicleNoAndIdNot(String vehicleNo, Integer id);
    
    List<DeliveryAgent> findByRatingGreaterThan(Integer rating);
}