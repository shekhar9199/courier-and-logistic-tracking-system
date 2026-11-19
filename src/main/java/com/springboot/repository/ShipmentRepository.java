package com.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Shipment;
import com.springboot.entity.ShipmentStatus;

public interface ShipmentRepository
        extends JpaRepository<Shipment, Integer> {

    boolean existsByTrackingNumber(String trackingNumber);

    boolean existsByCustomer_IdAndStatusIn(
            Integer customerId,
            List<ShipmentStatus> statuses
    );
    // FIX: Protect the customer foreign key by blocking deletion while any shipment exists.
    boolean existsByCustomer_Id(Integer customerId);
    
    boolean existsByDeliveryAgent_Id(Integer agentId);
    
    boolean existsByWarehouse_Id(Integer warehouseId);
    
    boolean existsByPayment_Id(Integer paymentId);
    
    // Find shipment by tracking number
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    
 // 8. Find shipments by Customer ID
    List<Shipment> findByCustomer_Id(Integer customerId);

    // 9. Find shipments by Warehouse ID
    List<Shipment> findByWarehouse_Id(Integer warehouseId);

    // 10. Find shipments by Delivery Agent ID
    List<Shipment> findByDeliveryAgent_Id(Integer agentId);
    
 // 11. Find by Source and Destination
    List<Shipment> findBySourceAndDestination(
            String source, String destination);

    // 12. Find by Delivery Date
    List<Shipment> findByDeliveryDateGreaterThanEqualAndDeliveryDateLessThan(
            LocalDateTime startDate, LocalDateTime endDate);

    
}