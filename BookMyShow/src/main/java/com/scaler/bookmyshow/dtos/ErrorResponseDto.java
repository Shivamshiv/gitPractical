package com.scaler.bookmyshow.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDto {
    private String status;
    private String message;

    public ErrorResponseDto(String message) {
        this.status = "FAILURE";
        this.message = message;
    }
}
