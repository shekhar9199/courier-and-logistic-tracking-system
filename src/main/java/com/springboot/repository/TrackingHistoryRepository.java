package com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.ShipmentStatus;
import com.springboot.entity.TrackingHistory;

public interface TrackingHistoryRepository
        extends JpaRepository<TrackingHistory, Integer> {
	
	// Get history by tracking number
    List<TrackingHistory> findByShipment_TrackingNumber(String trackingNumber);

    // Get history by status
    List<TrackingHistory> findByStatus(ShipmentStatus status);

    // Get all history of a particular shipment
    List<TrackingHistory> findByShipment_Id(Integer shipmentId);
}