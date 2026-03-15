package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.ShowNotFoundException;
import com.scaler.bookmyshow.models.Movie;
import com.scaler.bookmyshow.models.Show;
import com.scaler.bookmyshow.repositories.MovieRepository;
import com.scaler.bookmyshow.repositories.ShowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public MovieService(MovieRepository movieRepository, ShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Show> getShowsForMovie(Long movieId) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty()) {
            throw new ShowNotFoundException("Movie with ID: " + movieId + " not found.");
        }
        return showRepository.findAllByMovieId(movieId);
    }
}
