package com.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.entity.Shipment;
import com.springboot.entity.ShipmentStatus;
import com.springboot.entity.TrackingHistory;
import com.springboot.exception.IdNotFoundException;
import com.springboot.repository.ShipmentRepository;
import com.springboot.repository.TrackingHistoryRepository;

@Service
public class TrackingHistoryService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private TrackingHistoryRepository trackingHistoryRepository;

    @Transactional
    public List<TrackingHistory> saveAllHistory(Integer shipmentId, List<TrackingHistory> historyList) {
        // FIX: Validate the batch and return a domain not-found error, not a generic 500.
        if (historyList == null || historyList.isEmpty()) {
            throw new IllegalArgumentException("At least one tracking history record is required");
        }
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IdNotFoundException("Shipment not found with ID: " + shipmentId));
        for (TrackingHistory history : historyList) {
            if (history == null || history.getStatus() == null) {
                throw new IllegalArgumentException("Each tracking history record requires a status");
            }
            history.setId(null);
            history.setShipment(shipment);
        }
        return trackingHistoryRepository.saveAll(historyList);
    }

    public List<TrackingHistory> getAllHistory() {
        return trackingHistoryRepository.findAll();
    }

    public TrackingHistory getById(Integer id) {
        return trackingHistoryRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Tracking history not found with ID: " + id));
    }

    public List<TrackingHistory> getByTrackingNo(String trackingNo) {
        if (trackingNo == null || trackingNo.isBlank()) {
            throw new IllegalArgumentException("Tracking number is required");
        }
        return trackingHistoryRepository.findByShipment_TrackingNumber(trackingNo);
    }

    public List<TrackingHistory> getByStatus(ShipmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Shipment status is required");
        }
        return trackingHistoryRepository.findByStatus(status);
    }

    public List<TrackingHistory> getAllTracingHistoryOfShipment(Integer shipmentId) {
        if (!shipmentRepository.existsById(shipmentId)) {
            throw new IdNotFoundException("Shipment not found with ID: " + shipmentId);
        }
        return trackingHistoryRepository.findByShipment_Id(shipmentId);
    }
}
