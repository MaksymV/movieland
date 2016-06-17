package com.volomak.movieland.dao.postgres;

import com.volomak.movieland.dao.CountryDao;
import com.volomak.movieland.dao.GenreDao;
import com.volomak.movieland.dao.MovieDao;
import com.volomak.movieland.dao.ReviewDao;
import com.volomak.movieland.dao.postgres.mapper.MovieRowMapper;
import com.volomak.movieland.entity.Country;
import com.volomak.movieland.entity.Genre;
import com.volomak.movieland.entity.Movie;
import com.volomak.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieDaoImpl implements MovieDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getMovieByIdSQL;

    @Autowired
    private String getMoviesSQL;

    @Override
    public Movie getById(Long id) {
        log.info("Start query to get movie with id {} from DB", id);
        long startTime = System.currentTimeMillis();
        Movie movie = jdbcTemplate.queryForObject(getMovieByIdSQL, new Object[] {id}, new MovieRowMapper());
        enrichMovie(movie);
        log.info("Finish query to get movie with id {} from DB. It took {} ms", id, System.currentTimeMillis() - startTime);
        return movie;
    }

    @Override
    public List<Movie> getMovies() {
        log.info("Start query to get movies from DB");
        long startTime = System.currentTimeMillis();
        List<Movie> movies = jdbcTemplate.query(getMoviesSQL, new MovieRowMapper());
        for (Movie movie : movies) {
            enrichMovie(movie);
        }
        log.info("Finish query to get movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> search() {
        return null;
    }

    private Movie enrichMovie(Movie movie){
        List<Genre> genres = genreDao.getByMovieId(movie.getId());
        movie.setGenres(genres);
        List<Country> countries = countryDao.getByMovieId(movie.getId());
        movie.setCountries(countries);
        List<Review> reviews = reviewDao.getByMovieId(movie.getId());
        movie.setReviews(reviews);
        return movie;
    }
}