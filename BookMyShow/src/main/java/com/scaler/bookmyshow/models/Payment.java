package com.scaler.bookmyshow.models;

import com.scaler.bookmyshow.models.enums.PaymentProvider;
import com.scaler.bookmyshow.models.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment extends BaseModel {
    private String referenceNumber;
    
    @Transient
    private String paymentUrl;
    
    private double amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    @ManyToOne
    private Booking booking;
}
