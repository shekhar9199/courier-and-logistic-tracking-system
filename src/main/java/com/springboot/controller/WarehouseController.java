package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entity.Warehouse;
import com.springboot.service.WarehouseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
	
	@Autowired
	private WarehouseService warehouseService;
	
	@PostMapping("/create")
	public ResponseEntity<Warehouse> createWarehouse(
	        @Valid @RequestBody Warehouse warehouse) {

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(warehouseService.createWarehouse(warehouse));
	}
	@PostMapping("/saveAll")
	public ResponseEntity<List<Warehouse>> saveAllWarehouses(
	        @Valid @RequestBody List<@Valid Warehouse> warehouses) {

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(warehouseService.saveAllWarehouses(warehouses));
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<Warehouse>> getAllWarehouses() {

	    return ResponseEntity.ok(
	            warehouseService.getAllWarehouses()
	    );
	}
	@GetMapping("/get/{id}")
	public ResponseEntity<Warehouse> getWarehouseById(
	        @PathVariable Integer id) {

	    return ResponseEntity.ok(
	            warehouseService.getWarehouseById(id)
	    );
	}
	@GetMapping("/location/{location}")
	public ResponseEntity<List<Warehouse>> getWarehouseByLocation(
	        @PathVariable String location) {

	    return ResponseEntity.ok(
	            warehouseService.getWarehouseByLocation(location)
	    );
	}
	@GetMapping("/sortByCapacity")
	public ResponseEntity<List<Warehouse>> sortByCapacity() {

	    return ResponseEntity.ok(
	            warehouseService.sortByCapacity()
	    );
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<Warehouse> updateWarehouse(
	        @PathVariable Integer id,
	        @Valid @RequestBody Warehouse warehouse) {

	    return ResponseEntity.ok(
	            warehouseService.updateWarehouse(id, warehouse)
	    );
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteWarehouse(
	        @PathVariable Integer id) {

	    warehouseService.deleteWarehouse(id);

	    return ResponseEntity.ok("Warehouse deleted successfully");
	}
}
