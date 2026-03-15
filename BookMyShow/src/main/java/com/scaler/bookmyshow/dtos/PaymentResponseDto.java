package com.scaler.bookmyshow.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {
    private String paymentId;
    private String paymentLink;
    private String status;
}
