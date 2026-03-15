package com.scaler.bookmyshow.config;

import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.models.enums.*;
import com.scaler.bookmyshow.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;

    public DataLoader(RegionRepository regionRepository, TheaterRepository theaterRepository, 
                      ScreenRepository screenRepository, SeatRepository seatRepository, 
                      MovieRepository movieRepository, ShowRepository showRepository, 
                      ShowSeatRepository showSeatRepository, UserRepository userRepository) {
        this.regionRepository = regionRepository;
        this.theaterRepository = theaterRepository;
        this.screenRepository = screenRepository;
        this.seatRepository = seatRepository;
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Only insert if DB is empty
        if (regionRepository.count() == 0) {
            System.out.println("Initializing Dummy Data...");

            // 1. Create User
            User user = new User();
            user.setName("Dummy User");
            user.setEmail("dummy@example.com");
            user.setPassword("password123");
            userRepository.save(user);

            // 2. Create Region
            Region region = new Region();
            region.setName("Mumbai");
            regionRepository.save(region);

            // 3. Create Theater
            Theater theater = new Theater();
            theater.setName("PVR ICON Andheri");
            theater.setRegion(region);
            theaterRepository.save(theater);

            // 4. Create Screen
            Screen screen = new Screen();
            screen.setName("Screen 1");
            screen.setTheater(theater);
            List<Feature> features = new ArrayList<>();
            features.add(Feature.TWO_D);
            features.add(Feature.DOLBY);
            screen.setFeatures(features);
            screenRepository.save(screen);

            // 5. Create Seats for Screen
            List<Seat> seats = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Seat seat = new Seat();
                seat.setSeatNumber("A" + i);
                seat.setRowVal(1);
                seat.setColVal(i);
                seat.setSeatType(SeatType.VIP);
                seat.setScreen(screen);
                seats.add(seat);
            }
            seatRepository.saveAll(seats);

            // 6. Create Movie
            Movie movie = new Movie();
            movie.setName("Inception");
            movie.setDescription("A mind-bending thriller");
            movie.setDurationInMins(148);
            movie.setRating(8.8);
            movie.setLanguages(List.of("English", "Hindi"));
            movie.setFeatures(List.of(Feature.TWO_D, Feature.IMAX));
            movieRepository.save(movie);

            // 7. Create Show
            Show show = new Show();
            show.setMovie(movie);
            show.setScreen(screen);
            show.setStartTime(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow
            show.setEndTime(new Date(System.currentTimeMillis() + 86400000 + (148 * 60000)));
            showRepository.save(show);

            // 8. Create ShowSeats
            List<ShowSeat> showSeats = new ArrayList<>();
            for (Seat seat : seats) {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setShow(show);
                showSeat.setSeat(seat);
                showSeat.setStatus(ShowSeatStatus.AVAILABLE);
                showSeat.setPrice(500.0);
                showSeats.add(showSeat);
            }
            showSeatRepository.saveAll(showSeats);

            System.out.println("Dummy Data Initialization Complete!");
        }
    }
}
