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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;

    public BookingService(UserRepository userRepository,
                          ShowRepository showRepository,
                          ShowSeatRepository showSeatRepository,
                          BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(Long userId, Long showId, List<Long> showSeatIds) {

        // 1. Get User
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with ID: " + userId + " not found.");
        }
        User user = userOptional.get();

        // 2. Get Show
        Optional<Show> showOptional = showRepository.findById(showId);
        if (showOptional.isEmpty()) {
            throw new ShowNotFoundException("Show with ID: " + showId + " not found.");
        }
        Show show = showOptional.get();

        // 3. Get ShowSeats
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatIds);

        // 4. Check if all seats are Available
        for (ShowSeat showSeat : showSeats) {
            if (!(showSeat.getStatus().equals(ShowSeatStatus.AVAILABLE))) {
                throw new SeatNotAvailableException("Seat with ID: " + showSeat.getId() + " is already booked or blocked.");
            }
        }

        // 5. If available, change status to BLOCKED/IN_PROGRESS and Update DB
        for (ShowSeat showSeat : showSeats) {
            showSeat.setStatus(ShowSeatStatus.BLOCKED);
            showSeatRepository.save(showSeat);
        }

        // 6. Create booking with status PENDING/IN_PROGRESS
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setShowSeats(showSeats);
        booking.setBookingStatus(BookingStatus.IN_PROGRESS);

        // Calculate total amount
        double amount = 0;
        for (ShowSeat showSeat : showSeats) {
            amount += showSeat.getPrice();
        }
        booking.setAmount(amount);

        // Save booking to DB
        return bookingRepository.save(booking);
    }
}
