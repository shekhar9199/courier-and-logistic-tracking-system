package com.springboot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.entity.Customer;
import com.springboot.entity.ShipmentStatus;
import com.springboot.exception.DetailsAllreadyExistException;
import com.springboot.exception.IdNotFoundException;
import com.springboot.repository.CustomerRepository;
import com.springboot.repository.ShipmentRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ShipmentRepository shipmentRepository;

    public Customer saveCustomer(Customer customer) {

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DetailsAllreadyExistException("Email already exists");
        }

        if (customerRepository.existsByContact(customer.getContact())) {
            throw new DetailsAllreadyExistException("Contact number already exists");
        }

        return customerRepository.save(customer);
    }
    @Transactional
    public List<Customer> saveAllCustomers(List<Customer> customers) {

        Set<String> emails = new HashSet<>();
        Set<String> contacts = new HashSet<>();

        for (Customer customer : customers) {

            // Check duplicate email within request
            if (!emails.add(customer.getEmail())) {
                throw new DetailsAllreadyExistException(
                        "Duplicate email in request: " + customer.getEmail());
            }

            // Check duplicate contact within request
            if (!contacts.add(customer.getContact())) {
                throw new DetailsAllreadyExistException(
                        "Duplicate contact in request: " + customer.getContact());
            }

            // Check existing email in database
            if (customerRepository.existsByEmail(customer.getEmail())) {
                throw new DetailsAllreadyExistException(
                        "Email already exists: " + customer.getEmail());
            }

            // Check existing contact in database
            if (customerRepository.existsByContact(customer.getContact())) {
                throw new DetailsAllreadyExistException(
                        "Contact already exists: " + customer.getContact());
            }
        }

        return customerRepository.saveAll(customers);
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Customer getCustomerById(Integer id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                    new IdNotFoundException(
                        "Customer not found with ID: " + id
                    )
                );
    }
    
    public Customer getCustomerByEmail(String email) {

        return customerRepository.findByEmail(email)
                .orElseThrow(() ->
                    new IdNotFoundException(
                        "Customer not found with email: " + email
                    )
                );
    }
    @Transactional
    public Customer updateCustomerDetails(Integer id, Customer updatedCustomer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new IdNotFoundException(
                                "Customer not found with ID: " + id));

        // Check duplicate email, excluding the current customer
        if (customerRepository.existsByEmailAndIdNot(
                updatedCustomer.getEmail(), id)) {

            throw new DetailsAllreadyExistException("Email already exists");
        }

        // Check duplicate contact, excluding the current customer
        if (customerRepository.existsByContactAndIdNot(
                updatedCustomer.getContact(), id)) {

            throw new DetailsAllreadyExistException("Contact already exists");
        }

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setContact(updatedCustomer.getContact());
        existingCustomer.setAddress(updatedCustomer.getAddress());

        return customerRepository.save(existingCustomer);
    }
    
    @Transactional
    public void deleteCustomer(Integer id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                    new IdNotFoundException("Customer not found with ID: " + id));

        // FIX: Any linked shipment blocks deletion to avoid FK failures and orphaned records.
        if (shipmentRepository.existsByCustomer_Id(id)) {
            throw new IllegalStateException(
                    "Customer cannot be deleted because shipments are associated with this customer");
        }

        customerRepository.delete(customer);
    }
    public Customer getCustomerByContact(String contact) {

        return customerRepository.findByContact(contact)
                .orElseThrow(() -> new IdNotFoundException(
                        "Customer not found with contact: " + contact
                ));
    }
    public Page<Customer> getCustomersWithPaginationAndSorting(
            int pageNumber, int pageSize, String sortBy) {

        // FIX: Validate pagination and sort properties before Spring Data receives them.
        if (pageNumber < 0 || pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException("Page must be non-negative and page size must be between 1 and 100");
        }
        if (sortBy == null || !Set.of("id", "name", "email", "contact").contains(sortBy)) {
            throw new IllegalArgumentException("Invalid customer sort field: " + sortBy);
        }
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(sortBy).ascending()
        );

        return customerRepository.findAll(pageable);
    }
}
