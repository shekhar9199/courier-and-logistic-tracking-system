package com.springboot.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.ShipmentStatus;
import com.springboot.entity.TrackingHistory;
import com.springboot.service.TrackingHistoryService;

@RestController
@RequestMapping("/trackingHistory")
public class TrackingHistoryController {
	
	@Autowired
	private TrackingHistoryService trackingHistoryService;
	
	@PostMapping("/saveAll")
	public ResponseEntity<List<TrackingHistory>> saveAllHistory(
	        @RequestParam Integer shipmentId,
	        @RequestBody List<TrackingHistory> historyList) {
	    
	    // Pass BOTH shipmentId and historyList here
	    return ResponseEntity.ok(trackingHistoryService.saveAllHistory(shipmentId, historyList));
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<TrackingHistory>> getAllHistory() {

	    return ResponseEntity.ok(
	            trackingHistoryService.getAllHistory()
	    );
	}
	
	 // 1. Get by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<TrackingHistory> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(
                trackingHistoryService.getById(id));
    }

    // 2. Get by Tracking Number
    @GetMapping("/trackingNo/{trackingNo}")
    public ResponseEntity<List<TrackingHistory>> getByTrackingNo(
            @PathVariable String trackingNo) {

        return ResponseEntity.ok(
                trackingHistoryService.getByTrackingNo(trackingNo));
    }

    // 3. Get by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TrackingHistory>> getByStatus(
            @PathVariable ShipmentStatus status) {

        return ResponseEntity.ok(
                trackingHistoryService.getByStatus(status));
    }

    // 4. Get all history of a Shipment
    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<List<TrackingHistory>> getAllTracingHistoryOfShipment(
            @PathVariable Integer shipmentId) {

        return ResponseEntity.ok(
                trackingHistoryService
                    .getAllTracingHistoryOfShipment(shipmentId));
    }


}
