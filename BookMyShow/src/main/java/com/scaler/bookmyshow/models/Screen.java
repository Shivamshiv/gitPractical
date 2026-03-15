package com.scaler.bookmyshow.models;

import com.scaler.bookmyshow.models.enums.Feature;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Screen extends BaseModel {
    private String name;
    
    @ManyToOne
    private Theater theater;
    
    @OneToMany(mappedBy = "screen")
    private List<Seat> seats;

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection
    private List<Feature> features;
}
