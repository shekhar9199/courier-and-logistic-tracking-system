package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.entity.Payment;
import com.springboot.entity.PaymentStatus;
import com.springboot.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(
            @RequestBody Payment payment) {

        return new ResponseEntity<>(
                paymentService.createPayment(payment),
                HttpStatus.CREATED);
    }
 // 1. Get payment by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Payment> getPaymentById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id));
    }


    // 2. Get all payment details
    @GetMapping("/getAll")
    public ResponseEntity<List<Payment>> getAllPaymentDetails() {

        return ResponseEntity.ok(
                paymentService.getAllPaymentDetails());
    }


    // 3. Get payment details by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Payment>> getPaymentDetailsByCustomer(
            @PathVariable Integer customerId) {

        return ResponseEntity.ok(
                paymentService.getPaymentDetailsByCustomer(customerId));
    }


    // 4. Update payment status
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Integer id,
            @RequestParam PaymentStatus status) {

        return ResponseEntity.ok(
                paymentService.updatePaymentStatus(id, status));
    }
}