package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.CreateBookingRequestDto;
import com.scaler.bookmyshow.dtos.CreateBookingResponseDto;
import com.scaler.bookmyshow.models.Booking;
import com.scaler.bookmyshow.services.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @PostMapping
    public CreateBookingResponseDto createBooking(@RequestBody CreateBookingRequestDto requestDto) {
        Booking booking = bookingService.bookMovie(
                requestDto.getUserId(),
                requestDto.getShowId(),
                requestDto.getShowSeatIds()
        );
        
        CreateBookingResponseDto responseDto = new CreateBookingResponseDto();
        responseDto.setBookingId(booking.getId());
        responseDto.setAmount(booking.getAmount());
        responseDto.setStatus(booking.getBookingStatus());
        return responseDto;
    }
}
