package com.scaler.bookmyshow.dtos;

import com.scaler.bookmyshow.models.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingResponseDto {
    private Long bookingId;
    private double amount;
    private BookingStatus status;
}
