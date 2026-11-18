package com.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.Shipment;
import com.springboot.entity.ShipmentStatus;
import com.springboot.entity.TrackingHistory;
import com.springboot.repository.ShipmentRepository;
import com.springboot.repository.TrackingHistoryRepository;


@Service
public class TrackingHistoryService {

    @Autowired
    private ShipmentRepository shipmentRepository;
    
    @Autowired
    private TrackingHistoryRepository trackingHistoryRepository;

    public List<TrackingHistory> saveAllHistory(Integer shipmentId, List<TrackingHistory> historyList) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + shipmentId));

        for (TrackingHistory history : historyList) {
            history.setShipment(shipment);
        }

        return trackingHistoryRepository.saveAll(historyList);
    }

    public List<TrackingHistory> getAllHistory() {

        return trackingHistoryRepository.findAll();
    }
    

    // 1. Get history by ID
    public TrackingHistory getById(Integer id) {

        return trackingHistoryRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Tracking history not found with ID: " + id));
    }

    // 2. Get history by Tracking Number
    public List<TrackingHistory> getByTrackingNo(String trackingNo) {

        return trackingHistoryRepository
                .findByShipment_TrackingNumber(trackingNo);
    }

    // 3. Get history by Status
    public List<TrackingHistory> getByStatus(ShipmentStatus status) {

        return trackingHistoryRepository.findByStatus(status);
    }

    // 4. Get all Tracking History of a Shipment
    public List<TrackingHistory> getAllTracingHistoryOfShipment(Integer shipmentId) {

        return trackingHistoryRepository.findByShipment_Id(shipmentId);
    }
}
