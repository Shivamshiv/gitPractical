package com.scaler.bookmyshow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "_show")
public class Show extends BaseModel {
    @ManyToOne
    private Movie movie;
    
    @ManyToOne
    private Screen screen;
    
    private Date startTime;
    private Date endTime;
}
