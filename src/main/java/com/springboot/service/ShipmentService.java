
package com.springboot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.springboot.entity.Shipment;
import com.springboot.entity.TrackingHistory;
import com.springboot.entity.Warehouse;
import com.springboot.entity.ShipmentStatus;
import com.springboot.entity.DeliveryAgent;

import com.springboot.repository.ShipmentRepository;
import com.springboot.repository.WarehouseRepositoy;
import com.springboot.repository.CustomerRepository;
import com.springboot.repository.DeliveryAgentRepository;
import com.springboot.repository.PaymentRepository;

import com.springboot.exception.IdNotFoundException;
import com.springboot.exception.PaymentAlreadyUsedException;
import com.springboot.exception.DetailsAllreadyExistException;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WarehouseRepositoy warehouseRepository;

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public Shipment createShipment(Shipment shipment) {
        // FIX: Removed debug output and reject a null body before dereferencing it.
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment details are required");
        }
        // 1. Validate required route and tracking fields.
        if (shipment.getSource() == null || shipment.getSource().isBlank()
                || shipment.getDestination() == null || shipment.getDestination().isBlank()) {
            throw new IllegalArgumentException("Shipment source and destination are required");
        }
        if (shipment.getTrackingNumber() == null ||
                shipment.getTrackingNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Tracking number is required");
        }

        if (shipmentRepository.existsByTrackingNumber(
                shipment.getTrackingNumber())) {

            throw new DetailsAllreadyExistException(
                    "Tracking number already exists: "
                            + shipment.getTrackingNumber());
        }

        // 2. Validate customer
        if (shipment.getCustomer() == null ||
                shipment.getCustomer().getId() == null) {

            throw new IllegalArgumentException(
                    "Customer is required");
        }

        Integer customerId = shipment.getCustomer().getId();

        shipment.setCustomer(
                customerRepository.findById(customerId)
                        .orElseThrow(() -> new IdNotFoundException(
                                "Customer not found with ID: " + customerId))
        );

        // 3. Validate warehouse
        if (shipment.getWarehouse() == null ||
                shipment.getWarehouse().getId() == null) {

            throw new IllegalArgumentException(
                    "Warehouse is required");
        }

        Integer warehouseId = shipment.getWarehouse().getId();

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IdNotFoundException(
                        "Warehouse not found with ID: " + warehouseId));
        // FIX: Resolve the warehouse before assigning it; multiple shipments may share it.
        shipment.setWarehouse(warehouse);

        // 4. Validate delivery agent (optional)
        if (shipment.getDeliveryAgent() != null) {

            Integer agentId = shipment.getDeliveryAgent().getId();

            if (agentId == null) {
                throw new IllegalArgumentException(
                        "Delivery Agent ID is required");
            }

            DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
                    .orElseThrow(() -> new IdNotFoundException(
                            "Delivery Agent not found with ID: " + agentId));

            if (!Boolean.TRUE.equals(agent.getAvailabilityStatus())) {
                throw new IllegalStateException(
                        "Delivery Agent is not available");
            }

            shipment.setDeliveryAgent(agent);
        }

        // 5. Validate package
        if (shipment.getPackageEntity() == null) {

            throw new IllegalArgumentException(
                    "Package details are required");
        }

        // 6. Validate payment
        if (shipment.getPayment() == null ||
                shipment.getPayment().getId() == null) {

            throw new IllegalArgumentException(
                    "Payment ID is required");
        }

        Integer paymentId = shipment.getPayment().getId();

        shipment.setPayment(
                paymentRepository.findById(paymentId)
                        .orElseThrow(() -> new IdNotFoundException(
                                "Payment not found with ID: " + paymentId))
        );

        // 7. Validate weight
        if (shipment.getWeight() == null ||
                !Double.isFinite(shipment.getWeight()) || shipment.getWeight() <= 0) {

            throw new IllegalArgumentException(
                    "Shipment weight must be greater than zero");
        }

        // 8. Set initial status
        shipment.setStatus(ShipmentStatus.BOOKED);

        // FIX: Ignore client-supplied history; only server-generated history is trusted.
        shipment.setTrackingHistory(new ArrayList<>());

        // 10. Create initial tracking history
        TrackingHistory history = new TrackingHistory();

        history.setStatus(ShipmentStatus.BOOKED);
        history.setLocation(shipment.getSource());
        history.setRemarks("Shipment booked successfully");
        history.setShipment(shipment);

        shipment.getTrackingHistory().add(history);

        if (shipmentRepository.existsByPayment_Id(shipment.getPayment().getId())) {
            throw new PaymentAlreadyUsedException(
                "Payment ID " + shipment.getPayment().getId()
                + " is already used by another shipment."
            );
        }
        
        // 11. Save shipment
        return shipmentRepository.save(shipment);
    }
 // 1. Get all shipments
    public List<Shipment> getAllShipments() {

        return shipmentRepository.findAll();
    }

 // 2. Get shipment by ID
    public Shipment getById(Integer id) {

        return shipmentRepository.findById(id)
                .orElseThrow(() ->
                    new IdNotFoundException(
                        "Shipment not found with ID: " + id));
    }

    // 3. Get shipment by Tracking Number
    public Shipment getByTrackingNo(String trackingNo) {

        return shipmentRepository.findByTrackingNumber(trackingNo)
                .orElseThrow(() ->
                    new IdNotFoundException(
                        "Shipment not found with tracking number: " + trackingNo));
    }
    
 // 4. Update Shipment Status
    @Transactional
    public Shipment updateStatus(Integer shipmentId, ShipmentStatus status) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                    new IdNotFoundException("Shipment not found with ID: " + shipmentId));

        if (status == null) {
            throw new IllegalArgumentException("Shipment status is required");
        }

        // FIX: Keep the shipment's status and audit trail in sync.
        shipment.setStatus(status);
        TrackingHistory history = new TrackingHistory();
        history.setShipment(shipment);
        history.setStatus(status);
        history.setLocation(status == ShipmentStatus.DELIVERED ? shipment.getDestination() : null);
        history.setRemarks("Shipment status changed to " + status);
        shipment.getTrackingHistory().add(history);
        if (status == ShipmentStatus.DELIVERED && shipment.getDeliveryDate() == null) {
            shipment.setDeliveryDate(LocalDateTime.now());
        }
        return shipmentRepository.save(shipment);
    }


 // Get assigned Delivery Agent
    public DeliveryAgent getAssignedDeliveryAgent(Integer shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                    new IdNotFoundException("Shipment not found with ID: " + shipmentId));

        if (shipment.getDeliveryAgent() == null) {
            throw new IdNotFoundException("No delivery agent assigned to this shipment");
        }

        return shipment.getDeliveryAgent();
    }


    // Get assigned Warehouse
    public Warehouse getAssignedWarehouse(Integer shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                    new IdNotFoundException("Shipment not found with ID: " + shipmentId));

        if (shipment.getWarehouse() == null) {
            throw new IdNotFoundException("No warehouse assigned to this shipment");
        }

        return shipment.getWarehouse();
    }
    
 // 8. Get shipments by Customer ID
    public List<Shipment> getByCustomerId(Integer customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new IdNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        return shipmentRepository.findByCustomer_Id(customerId);
    }


    // 9. Get shipments by Warehouse ID
    public List<Shipment> getByWarehouseId(Integer warehouseId) {

        if (!warehouseRepository.existsById(warehouseId)) {
            throw new IdNotFoundException(
                    "Warehouse not found with ID: " + warehouseId);
        }

        return shipmentRepository.findByWarehouse_Id(warehouseId);
    }


    // 10. Get shipments by Delivery Agent ID
    public List<Shipment> getByDeliveryAgent(Integer agentId) {

        if (!deliveryAgentRepository.existsById(agentId)) {
            throw new IdNotFoundException(
                    "Delivery Agent not found with ID: " + agentId);
        }

        return shipmentRepository.findByDeliveryAgent_Id(agentId);
    }
    
 // 11. Get shipments by Source and Destination
    public List<Shipment> getBySourceAndDestination(
            String source, String destination) {

        if (source == null || source.isBlank()
                || destination == null || destination.isBlank()) {
            throw new IllegalArgumentException(
                    "Source and destination are required");
        }

        return shipmentRepository.findBySourceAndDestination(
                source, destination);
    }


    // 12. Get shipments by Delivery Date
    public List<Shipment> getByDeliveryDate(LocalDate date) {
    	

        if (date == null) {
            throw new IllegalArgumentException("Delivery date is required");
        }

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.plusDays(1).atStartOfDay();

        return shipmentRepository
                .findByDeliveryDateGreaterThanEqualAndDeliveryDateLessThan(
                        startDate, endDate);
    }


    // 13. Get shipments with Pagination and Sorting
    public Page<Shipment> getByPaginationAndSorting(
            int page, int size, String sortBy, String direction) {

        if (page < 0 || size <= 0 || size > 100) {
            throw new IllegalArgumentException(
                    "Page must be 0 or greater and size must be between 1 and 100");
        }

        Set<String> allowedFields = Set.of(
                "id", "trackingNumber", "source", "destination",
                "weight", "shipmentDateTime", "deliveryDate", "status"
        );

        if (sortBy == null || !allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sorting field: " + sortBy);
        }

        if (direction == null || (!direction.equalsIgnoreCase("ASC")
                && !direction.equalsIgnoreCase("DESC"))) {
            throw new IllegalArgumentException(
                    "Direction must be ASC or DESC");
        }

        Sort sort = Sort.by(
                Sort.Direction.valueOf(direction.toUpperCase()), sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        return shipmentRepository.findAll(pageable);
    }
    
 // 14. Delete Shipment
    public String deleteShipment(Integer shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                    new IdNotFoundException(
                        "Shipment not found with ID: " + shipmentId));

        shipmentRepository.delete(shipment);

        return "Shipment deleted successfully with ID: " + shipmentId;
    }


    // 15. Update Destination
    public Shipment updateDestination(Integer shipmentId, String destination) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                    new IdNotFoundException(
                        "Shipment not found with ID: " + shipmentId));

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException(
                    "Destination cannot be empty");
        }

        shipment.setDestination(destination.trim());

        return shipmentRepository.save(shipment);
    }


    // 16. Get All Shipments of a Customer
    public List<Shipment> getAllShipmentOfACustomer(Integer customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new IdNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        return shipmentRepository.findByCustomer_Id(customerId);
    }
}
