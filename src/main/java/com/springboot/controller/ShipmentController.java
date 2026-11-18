package com.springboot.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.DeliveryAgent;
import com.springboot.entity.Shipment;
import com.springboot.entity.ShipmentStatus;
import com.springboot.entity.Warehouse;
import com.springboot.service.ShipmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
	
	@Autowired
	private ShipmentService shipmentService;
	
	
	@PostMapping("/create")
	public ResponseEntity<Shipment> createShipment(
	        @Valid @RequestBody Shipment shipment) {

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(shipmentService.createShipment(shipment));
	}
	
	 // 1. Get all shipments
    @GetMapping("/getAll")
    public ResponseEntity<List<Shipment>> getAllShipments() {

        return ResponseEntity.ok(
                shipmentService.getAllShipments());
    }

    // 2. Get shipment by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Shipment> getById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                shipmentService.getById(id));
    }

    // 3. Get shipment by Tracking Number
    @GetMapping("/trackingNo/{trackingNo}")
    public ResponseEntity<Shipment> getByTrackingNo(
            @PathVariable String trackingNo) {

        return ResponseEntity.ok(
                shipmentService.getByTrackingNo(trackingNo));
    }	
    
 // 4. Update Shipment Status
    @PutMapping("/updateStatus/{shipmentId}")
    public ResponseEntity<Shipment> updateStatus(
            @PathVariable Integer shipmentId,
            @RequestParam ShipmentStatus status) {

        return ResponseEntity.ok(
                shipmentService.updateStatus(shipmentId, status));
    }


 // Check assigned Delivery Agent
    @GetMapping("/assignedAgent/{shipmentId}")
    public ResponseEntity<DeliveryAgent> getAssignedDeliveryAgent(
            @PathVariable Integer shipmentId) {

        return ResponseEntity.ok(
                shipmentService.getAssignedDeliveryAgent(shipmentId));
    }


    // Check assigned Warehouse
    @GetMapping("/assignedWarehouse/{shipmentId}")
    public ResponseEntity<Warehouse> getAssignedWarehouse(
            @PathVariable Integer shipmentId) {

        return ResponseEntity.ok(
                shipmentService.getAssignedWarehouse(shipmentId));
    }
 // 8. Get shipments by Customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Shipment>> getByCustomerId(
            @PathVariable Integer customerId) {

        return ResponseEntity.ok(
                shipmentService.getByCustomerId(customerId));
    }


    // 9. Get shipments by Warehouse ID
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Shipment>> getByWarehouseId(
            @PathVariable Integer warehouseId) {

        return ResponseEntity.ok(
                shipmentService.getByWarehouseId(warehouseId));
    }


    // 10. Get shipments by Delivery Agent ID
    @GetMapping("/deliveryAgent/{agentId}")
    public ResponseEntity<List<Shipment>> getByDeliveryAgent(
            @PathVariable Integer agentId) {

        return ResponseEntity.ok(
                shipmentService.getByDeliveryAgent(agentId));
    }
    
 // 11. Get by Source and Destination
    @GetMapping("/route")
    public ResponseEntity<List<Shipment>> getBySourceAndDestination(
            @RequestParam String source,
            @RequestParam String destination) {

        return ResponseEntity.ok(
                shipmentService.getBySourceAndDestination(source, destination));
    }


    // 12. Get by Delivery Date
    @GetMapping("/deliveryDate")
    public ResponseEntity<List<Shipment>> getByDeliveryDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                shipmentService.getByDeliveryDate(date));
    }


    // 13. Pagination and Sorting
    @GetMapping("/pagination")
    public ResponseEntity<Page<Shipment>> getByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return ResponseEntity.ok(
                shipmentService.getByPaginationAndSorting(
                        page, size, sortBy, direction));
    }
    
 // 14. Delete Shipment
    @DeleteMapping("/delete/{shipmentId}")
    public ResponseEntity<String> deleteShipment(
            @PathVariable Integer shipmentId) {

        return ResponseEntity.ok(
                shipmentService.deleteShipment(shipmentId));
    }


    // 15. Update Destination
    @PutMapping("/updateDestination/{shipmentId}")
    public ResponseEntity<Shipment> updateDestination(
            @PathVariable Integer shipmentId,
            @RequestParam String destination) {

        return ResponseEntity.ok(
                shipmentService.updateDestination(shipmentId, destination));
    }


    // 16. Get All Shipments of a Customer
    @GetMapping("/customer/all/{customerId}")
    public ResponseEntity<List<Shipment>> getAllShipmentOfACustomer(
            @PathVariable Integer customerId) {

        return ResponseEntity.ok(
                shipmentService.getAllShipmentOfACustomer(customerId));
    }
}
