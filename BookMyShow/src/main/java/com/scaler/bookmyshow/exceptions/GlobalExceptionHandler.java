package com.scaler.bookmyshow.exceptions;

import com.scaler.bookmyshow.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidBookingException(InvalidBookingException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShowNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleShowNotFoundException(ShowNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeatNotAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleSeatNotAvailableException(SeatNotAvailableException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponseDto("An unexpected error occurred: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
