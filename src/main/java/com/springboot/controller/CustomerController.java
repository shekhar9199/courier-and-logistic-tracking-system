package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.springboot.entity.Customer;
import com.springboot.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/save")
	public ResponseEntity<Customer> saveCustomer(
	        @Valid @RequestBody Customer customer) {

	    return ResponseEntity.ok(customerService.saveCustomer(customer));
	}
	@PostMapping("/saveAll")
	public ResponseEntity<List<Customer>> saveAllCustomers(
	        @Valid @RequestBody List<@Valid Customer> customers) {

	    return ResponseEntity.ok(
	            customerService.saveAllCustomers(customers));
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<Customer>> getAllCustomers() {

	    return ResponseEntity.ok(customerService.getAllCustomers());
	}
	@GetMapping("/get/{id}")
	public ResponseEntity<Customer> getCustomerById(
	        @PathVariable Integer id) {

	    return ResponseEntity.ok(
	            customerService.getCustomerById(id)
	    );
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<Customer> getCustomerByEmail(
	        @PathVariable String email) {

	    return ResponseEntity.ok(
	            customerService.getCustomerByEmail(email)
	    );
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<Customer> updateCustomerDetails(
	        @PathVariable Integer id,
	        @Valid @RequestBody Customer customer) {

	    return ResponseEntity.ok(
	            customerService.updateCustomerDetails(id, customer));
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {

	    customerService.deleteCustomer(id);

	    return ResponseEntity.ok("Customer deleted successfully");
	}
	@GetMapping("/contact/{contact}")
	public ResponseEntity<Customer> getCustomerByContact(
	        @PathVariable String contact) {

	    return ResponseEntity.ok(
	            customerService.getCustomerByContact(contact)
	    );
	}
	@GetMapping("/pagination")
	public ResponseEntity<Page<Customer>> getCustomersWithPaginationAndSorting(
	        @RequestParam(defaultValue = "0") int pageNumber,
	        @RequestParam(defaultValue = "5") int pageSize,
	        @RequestParam(defaultValue = "id") String sortBy) {

	    return ResponseEntity.ok(
	            customerService.getCustomersWithPaginationAndSorting(
	                    pageNumber, pageSize, sortBy)
	    );
	}
	
}
