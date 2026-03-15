package com.scaler.bookmyshow.models;

import com.scaler.bookmyshow.models.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Booking extends BaseModel {
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Show show;
    
    @ManyToMany
    private List<ShowSeat> showSeats;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    
    private double amount;
    
    @OneToMany(mappedBy = "booking")
    private List<Payment> payments;
}
