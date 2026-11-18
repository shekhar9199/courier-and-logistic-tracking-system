package com.springboot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.entity.DeliveryAgent;
import com.springboot.exception.DetailsAllreadyExistException;
import com.springboot.exception.IdNotFoundException;
import com.springboot.repository.DeliveryAgentRepository;
import com.springboot.repository.ShipmentRepository;

import jakarta.transaction.Transactional;

@Service
public class DeliveryAgentService {
	
	@Autowired
	private DeliveryAgentRepository deliveryAgentRepository;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Transactional
	public List<DeliveryAgent> saveAllAgents(List<DeliveryAgent> agents) {

	    Set<String> contacts = new HashSet<>();
	    Set<String> vehicleNumbers = new HashSet<>();

	    for (DeliveryAgent agent : agents) {

	        // Check duplicate contacts inside request
	        if (!contacts.add(agent.getContact())) {
	            throw new DetailsAllreadyExistException(
	                    "Duplicate contact in request: " + agent.getContact());
	        }

	        // Check duplicate vehicle numbers inside request
	        if (!vehicleNumbers.add(agent.getVehicleNo())) {
	            throw new DetailsAllreadyExistException(
	                    "Duplicate vehicle number in request: " + agent.getVehicleNo());
	        }

	        // Check contact already exists in database
	        if (deliveryAgentRepository.existsByContact(agent.getContact())) {
	            throw new DetailsAllreadyExistException(
	                    "Contact already exists: " + agent.getContact());
	        }

	        // Check vehicle number already exists in database
	        if (deliveryAgentRepository.existsByVehicleNo(agent.getVehicleNo())) {
	            throw new DetailsAllreadyExistException(
	                    "Vehicle number already exists: " + agent.getVehicleNo());
	        }
	    }

	    return deliveryAgentRepository.saveAll(agents);
	}
	public List<DeliveryAgent> getAllAgents() {
	    return deliveryAgentRepository.findAll();
	}
	
	public DeliveryAgent getAgentById(Integer id) {

	    return deliveryAgentRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Delivery Agent not found with ID: " + id
	            ));
	}
	
	public DeliveryAgent getAgentByVehicleNo(String vehicleNo) {

	    return deliveryAgentRepository.findByVehicleNo(vehicleNo)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Delivery Agent not found with vehicle number: " + vehicleNo
	            ));
	}
	
	public DeliveryAgent getAgentByContact(String contact) {

	    return deliveryAgentRepository.findByContact(contact)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Delivery Agent not found with contact: " + contact
	            ));
	}
	
	@Transactional
	public DeliveryAgent updateAgentDetails(
	        Integer id, DeliveryAgent updatedAgent) {

	    DeliveryAgent existingAgent = deliveryAgentRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Delivery Agent not found with ID: " + id));

	    // Check duplicate contact
	    if (deliveryAgentRepository.existsByContactAndIdNot(
	            updatedAgent.getContact(), id)) {

	        throw new DetailsAllreadyExistException(
	                "Contact already exists: " + updatedAgent.getContact());
	    }

	    // Check duplicate vehicle number
	    if (deliveryAgentRepository.existsByVehicleNoAndIdNot(
	            updatedAgent.getVehicleNo(), id)) {

	        throw new DetailsAllreadyExistException(
	                "Vehicle number already exists: " + updatedAgent.getVehicleNo());
	    }

	    existingAgent.setName(updatedAgent.getName());
	    existingAgent.setContact(updatedAgent.getContact());
	    existingAgent.setVehicleNo(updatedAgent.getVehicleNo());
	    existingAgent.setAvailabilityStatus(updatedAgent.getAvailabilityStatus());
	    existingAgent.setRating(updatedAgent.getRating());

	    return deliveryAgentRepository.save(existingAgent);
	}
	
	@Transactional
	public void deleteAgent(Integer id) {

	    DeliveryAgent agent = deliveryAgentRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Delivery Agent not found with ID: " + id
	            ));

	    boolean hasShipments =
	            shipmentRepository.existsByDeliveryAgent_Id(id);

	    if (hasShipments) {
	        throw new IllegalStateException(
	                "Cannot delete agent because shipments are assigned to this agent"
	        );
	    }

	    deliveryAgentRepository.delete(agent);
	}
	@Transactional
	public DeliveryAgent updateAvailability(Integer id, Boolean availabilityStatus) {

	    DeliveryAgent agent = deliveryAgentRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Delivery Agent not found with ID: " + id
	            ));

	    agent.setAvailabilityStatus(availabilityStatus);

	    return deliveryAgentRepository.save(agent);
	}
	public List<DeliveryAgent> getAgentsByRatingGreaterThan(Integer rating) {

	    return deliveryAgentRepository.findByRatingGreaterThan(rating);
	}
	public List<DeliveryAgent> sortByRating() {

	    Sort sort = Sort.by("rating").descending();

	    return deliveryAgentRepository.findAll(sort);
	}
}
