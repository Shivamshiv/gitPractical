package com.scaler.bookmyshow.models;

import com.scaler.bookmyshow.models.enums.Feature;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Movie extends BaseModel {
    private String name;
    private String description;
    private int durationInMins;
    private double rating;
    
    @ElementCollection
    private List<String> languages;
    
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<Feature> features;
}
