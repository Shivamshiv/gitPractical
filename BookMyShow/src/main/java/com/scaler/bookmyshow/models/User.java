package com.scaler.bookmyshow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;
}
