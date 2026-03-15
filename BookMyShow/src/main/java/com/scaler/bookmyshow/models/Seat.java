package com.scaler.bookmyshow.models;

import com.scaler.bookmyshow.models.enums.SeatType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Seat extends BaseModel {
    private String seatNumber;
    
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    
    private int rowVal;
    private int colVal;
    
    @ManyToOne
    private Screen screen;
}
