
package com.springboot.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$",
             message = "Contact number must contain exactly 10 digits")
    @Column(unique = true, length = 10, nullable = false)
    private String contact;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String vehicleNo;

    @Column(nullable = false)
    private Boolean availabilityStatus = true;

    private Double rating;
    
    // FIX: Prevent circular JSON serialization through Shipment.deliveryAgent.
    @OneToMany(mappedBy = "deliveryAgent")
    @JsonIgnore
    private List<Shipment> shipments;
}
