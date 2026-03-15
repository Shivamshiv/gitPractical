package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.SeatNotAvailableException;
import com.scaler.bookmyshow.exceptions.ShowNotFoundException;
import com.scaler.bookmyshow.exceptions.UserNotFoundException;
import com.scaler.bookmyshow.models.Booking;
import com.scaler.bookmyshow.models.Show;
import com.scaler.bookmyshow.models.ShowSeat;
import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.models.enums.BookingStatus;
import com.scaler.bookmyshow.models.enums.ShowSeatStatus;
import com.scaler.bookmyshow.repositories.BookingRepository;
import com.scaler.bookmyshow.repositories.ShowRepository;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import com.scaler.bookmyshow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookMovie_Success() {
        // Arrange
        Long userId = 1L;
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L);

        User user = new User(); user.setId(userId);
        Show show = new Show(); show.setId(showId);

        ShowSeat seat1 = new ShowSeat(); seat1.setId(1L); seat1.setStatus(ShowSeatStatus.AVAILABLE); seat1.setPrice(100.0);
        ShowSeat seat2 = new ShowSeat(); seat2.setId(2L); seat2.setStatus(ShowSeatStatus.AVAILABLE); seat2.setPrice(150.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAllById(showSeatIds)).thenReturn(Arrays.asList(seat1, seat2));

        Booking mockBooking = new Booking();
        mockBooking.setId(10L);
        mockBooking.setAmount(250.0);
        mockBooking.setBookingStatus(BookingStatus.IN_PROGRESS);

        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        // Act
        Booking booking = bookingService.bookMovie(userId, showId, showSeatIds);

        // Assert
        assertNotNull(booking);
        assertEquals(10L, booking.getId());
        assertEquals(250.0, booking.getAmount());
        assertEquals(BookingStatus.IN_PROGRESS, booking.getBookingStatus());
        
        verify(showSeatRepository, times(2)).save(any(ShowSeat.class));
        assertEquals(ShowSeatStatus.BLOCKED, seat1.getStatus());
        assertEquals(ShowSeatStatus.BLOCKED, seat2.getStatus());
    }

    @Test
    void bookMovie_UserNotFound_ThrowsException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> bookingService.bookMovie(userId, 1L, List.of(1L)));
    }

    @Test
    void bookMovie_ShowNotFound_ThrowsException() {
        // Arrange
        Long userId = 1L;
        Long showId = 1L;
        User user = new User(); user.setId(userId);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(showRepository.findById(showId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShowNotFoundException.class, () -> bookingService.bookMovie(userId, showId, List.of(1L)));
    }

    @Test
    void bookMovie_SeatNotAvailable_ThrowsException() {
        // Arrange
        Long userId = 1L;
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L);

        User user = new User(); user.setId(userId);
        Show show = new Show(); show.setId(showId);

        ShowSeat seat1 = new ShowSeat(); seat1.setId(1L); seat1.setStatus(ShowSeatStatus.AVAILABLE);
        ShowSeat seat2 = new ShowSeat(); seat2.setId(2L); seat2.setStatus(ShowSeatStatus.BOOKED); // Not available!

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAllById(showSeatIds)).thenReturn(Arrays.asList(seat1, seat2));

        // Act & Assert
        assertThrows(SeatNotAvailableException.class, () -> bookingService.bookMovie(userId, showId, showSeatIds));
        verify(showSeatRepository, never()).save(any());
        verify(bookingRepository, never()).save(any());
    }
}
