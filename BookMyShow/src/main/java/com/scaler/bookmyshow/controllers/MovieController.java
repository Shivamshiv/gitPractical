package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.models.Movie;
import com.scaler.bookmyshow.models.Show;
import com.scaler.bookmyshow.services.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{movieId}/shows")
    public List<Show> getShowsForMovie(@PathVariable Long movieId) {
        return movieService.getShowsForMovie(movieId);
    }
}
