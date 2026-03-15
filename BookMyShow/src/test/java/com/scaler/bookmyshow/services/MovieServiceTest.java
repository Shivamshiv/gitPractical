package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.ShowNotFoundException;
import com.scaler.bookmyshow.models.Movie;
import com.scaler.bookmyshow.models.Show;
import com.scaler.bookmyshow.repositories.MovieRepository;
import com.scaler.bookmyshow.repositories.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowRepository showRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMovies_Success() {
        // Arrange
        Movie m1 = new Movie(); m1.setName("M1");
        Movie m2 = new Movie(); m2.setName("M2");
        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        // Act
        List<Movie> movies = movieService.getAllMovies();

        // Assert
        assertEquals(2, movies.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getShowsForMovie_MovieExists_Success() {
        // Arrange
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);

        Show s1 = new Show(); s1.setId(10L);
        Show s2 = new Show(); s2.setId(20L);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(showRepository.findAllByMovieId(movieId)).thenReturn(Arrays.asList(s1, s2));

        // Act
        List<Show> shows = movieService.getShowsForMovie(movieId);

        // Assert
        assertEquals(2, shows.size());
        verify(movieRepository, times(1)).findById(movieId);
        verify(showRepository, times(1)).findAllByMovieId(movieId);
    }

    @Test
    void getShowsForMovie_MovieNotFound_ThrowsException() {
        // Arrange
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShowNotFoundException.class, () -> movieService.getShowsForMovie(movieId));
        verify(movieRepository, times(1)).findById(movieId);
        verify(showRepository, never()).findAllByMovieId(anyLong());
    }
}
