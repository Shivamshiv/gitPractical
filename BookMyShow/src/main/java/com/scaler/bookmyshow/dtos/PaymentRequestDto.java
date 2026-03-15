package com.scaler.bookmyshow.dtos;

import com.scaler.bookmyshow.models.enums.PaymentProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private Long bookingId;
    private double amount;
    private PaymentProvider paymentProvider;
}
