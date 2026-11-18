package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.DeliveryAgent;
import com.springboot.service.DeliveryAgentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/deliveryAgent")
public class DeliveryAgentController {
	
	@Autowired
	private DeliveryAgentService deliveryAgentService;
	
	@PostMapping("/saveAll")
	public ResponseEntity<List<DeliveryAgent>> saveAllAgents(
	        @Valid @RequestBody List<@Valid DeliveryAgent> agents) {

	    return ResponseEntity.ok(
	            deliveryAgentService.saveAllAgents(agents));
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<DeliveryAgent>> getAllAgents() {

	    return ResponseEntity.ok(
	            deliveryAgentService.getAllAgents()
	    );
	}
	@GetMapping("/get/{id}")
	public ResponseEntity<DeliveryAgent> getAgentById(
	        @PathVariable Integer id) {

	    return ResponseEntity.ok(
	            deliveryAgentService.getAgentById(id)
	    );
	}
	@GetMapping("/vehicle/{vehicleNo}")
	public ResponseEntity<DeliveryAgent> getAgentByVehicleNo(
	        @PathVariable String vehicleNo) {

	    return ResponseEntity.ok(
	            deliveryAgentService.getAgentByVehicleNo(vehicleNo)
	    );
	}
	@GetMapping("/contact/{contact}")
	public ResponseEntity<DeliveryAgent> getAgentByContact(
	        @PathVariable String contact) {

	    return ResponseEntity.ok(
	            deliveryAgentService.getAgentByContact(contact)
	    );
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<DeliveryAgent> updateAgentDetails(
	        @PathVariable Integer id,
	        @Valid @RequestBody DeliveryAgent agent) {

	    return ResponseEntity.ok(
	            deliveryAgentService.updateAgentDetails(id, agent));
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteAgent(@PathVariable Integer id) {

	    deliveryAgentService.deleteAgent(id);

	    return ResponseEntity.ok("Delivery Agent deleted successfully");
	}
	@PatchMapping("/availability/{id}")
	public ResponseEntity<DeliveryAgent> updateAvailability(
	        @PathVariable Integer id,
	        @RequestParam Boolean availabilityStatus) {

	    return ResponseEntity.ok(
	            deliveryAgentService.updateAvailability(id, availabilityStatus)
	    );
	}
	@GetMapping("/rating/greaterThan/{rating}")
	public ResponseEntity<List<DeliveryAgent>> getAgentsByRatingGreaterThan(
	        @PathVariable Integer rating) {

	    return ResponseEntity.ok(
	            deliveryAgentService.getAgentsByRatingGreaterThan(rating)
	    );
	}
	@GetMapping("/sortByRating")
	public ResponseEntity<List<DeliveryAgent>> sortByRating() {

	    return ResponseEntity.ok(
	            deliveryAgentService.sortByRating()
	    );
	}
}
