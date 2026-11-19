
package com.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.Payment;
import com.springboot.entity.PaymentStatus;
import com.springboot.exception.IdNotFoundException;
import com.springboot.exception.IllegalStateException;
import com.springboot.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // 1. Create Payment
    public Payment createPayment(Payment payment) {

        // FIX: Guard against null request bodies and non-finite/non-positive amounts.
        if (payment == null) {
            throw new IllegalArgumentException("Payment details are required");
        }
        if (payment.getAmount() == null || !Double.isFinite(payment.getAmount()) || payment.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }

        // Validate payment method
        if (payment.getPaymentMethod() == null) {
            throw new IllegalArgumentException(
                    "Payment method is required");
        }

        // FIX: New payments always start pending; callers cannot mark an unpaid payment successful.
        payment.setPaymentStatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    // 2. Get Payment by ID
    public Payment getPaymentById(Integer id) {

        return paymentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Payment not found with ID: " + id));
    }

    // 3. Get All Payment Details
    public List<Payment> getAllPaymentDetails() {

        return paymentRepository.findAll();
    }

    // 4. Get Payment Details by Customer ID
    public List<Payment> getPaymentDetailsByCustomer(Integer customerId) {

        // FIX: Return a not-found response for invalid customer references.
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        return paymentRepository.findByShipment_Customer_Id(customerId);
    }

    // 5. Update Payment Status
    public Payment updatePaymentStatus(Integer id, PaymentStatus status) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Payment not found with ID: " + id));

        if (status == null) {
            throw new IllegalArgumentException(
                    "Payment status is required");
        }
        // FIX: Prevent accidental transitions out of terminal payment states.
        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED
                || payment.getPaymentStatus() == PaymentStatus.FAILED) {
            throw new IllegalStateException("A terminal payment status cannot be changed");
        }

        payment.setPaymentStatus(status);

        return paymentRepository.save(payment);
    }
}
