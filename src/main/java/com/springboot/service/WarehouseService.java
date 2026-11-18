package com.springboot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.entity.Warehouse;
import com.springboot.exception.DetailsAllreadyExistException;
import com.springboot.exception.IdNotFoundException;
import com.springboot.repository.ShipmentRepository;
import com.springboot.repository.WarehouseRepositoy;

import jakarta.transaction.Transactional;

@Service
public class WarehouseService {
	
	@Autowired
	private WarehouseRepositoy warehouseRepository;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	public Warehouse createWarehouse(Warehouse warehouse) {

	    if (warehouseRepository.existsByContact(warehouse.getContact())) {
	        throw new DetailsAllreadyExistException(
	                "Warehouse contact already exists: " + warehouse.getContact()
	        );
	    }

	    return warehouseRepository.save(warehouse);
	}
	
	@Transactional
	public List<Warehouse> saveAllWarehouses(List<Warehouse> warehouses) {

	    Set<String> contacts = new HashSet<>();

	    for (Warehouse warehouse : warehouses) {

	        // Check duplicate contacts inside request
	        if (!contacts.add(warehouse.getContact())) {
	            throw new DetailsAllreadyExistException(
	                    "Duplicate contact in request: " + warehouse.getContact()
	            );
	        }

	        // Check duplicate contacts in database
	        if (warehouseRepository.existsByContact(warehouse.getContact())) {
	            throw new DetailsAllreadyExistException(
	                    "Warehouse contact already exists: " + warehouse.getContact()
	            );
	        }
	    }

	    return warehouseRepository.saveAll(warehouses);
	}
	public List<Warehouse> getAllWarehouses() {
	    return warehouseRepository.findAll();
	}
	public Warehouse getWarehouseById(Integer id) {

	    return warehouseRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Warehouse not found with ID: " + id
	            ));
	}
	public List<Warehouse> getWarehouseByLocation(String location) {

	    return warehouseRepository.findByLocation(location);
	}
	public List<Warehouse> sortByCapacity() {

	    Sort sort = Sort.by("capacity").descending();

	    return warehouseRepository.findAll(sort);
	}
	
	@Transactional
	public Warehouse updateWarehouse(Integer id, Warehouse updatedWarehouse) {

	    Warehouse existingWarehouse = warehouseRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Warehouse not found with ID: " + id
	            ));

	    if (warehouseRepository.existsByContactAndIdNot(
	            updatedWarehouse.getContact(), id)) {

	        throw new DetailsAllreadyExistException(
	                "Warehouse contact already exists: "
	                + updatedWarehouse.getContact()
	        );
	    }

	    existingWarehouse.setName(updatedWarehouse.getName());
	    existingWarehouse.setLocation(updatedWarehouse.getLocation());
	    existingWarehouse.setCapacity(updatedWarehouse.getCapacity());
	    existingWarehouse.setContact(updatedWarehouse.getContact());

	    return warehouseRepository.save(existingWarehouse);
	}
	
	@Transactional
	public void deleteWarehouse(Integer id) {

	    Warehouse warehouse = warehouseRepository.findById(id)
	            .orElseThrow(() -> new IdNotFoundException(
	                    "Warehouse not found with ID: " + id
	            ));

	    boolean hasShipment =
	            shipmentRepository.existsByWarehouse_Id(id);

	    if (hasShipment) {
	        throw new IllegalStateException(
	                "Cannot delete warehouse because shipments are associated with it"
	        );
	    }

	    warehouseRepository.delete(warehouse);
	}
}
