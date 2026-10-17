package com.viratech.service;

import com.viratech.dto.MovieFilterRequest;
import com.viratech.dto.MoviePageResponse;
import com.viratech.dto.MovieRequest;
import com.viratech.dto.MovieResponse;
import com.viratech.enums.Genre;
import com.viratech.exceptions.MovieNotFoundException;
import com.viratech.mapper.MovieMapper;
import com.viratech.model.Movie;
import com.viratech.repository.MovieRepository;
import com.viratech.specification.MovieSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repository;
    private final MovieMapper mapper;

    public MovieResponse create(MovieRequest request){
        Movie movie = repository.save(mapper.toEntity(request));
        return mapper.toDto(movie);
    }

    public MoviePageResponse listAllMovies(MovieFilterRequest filter, Pageable pageable){

        Specification<Movie> spec = MovieSpecification.filter(filter);

        Page<Movie> movies = repository.findAll(spec, pageable);

        List<MovieResponse> itens = movies.getContent()
                .stream()
                .map(mapper::toDto).toList();

        return new MoviePageResponse(itens,
                movies.getTotalElements(),
                movies.getTotalPages(),
                movies.getNumber(),
                movies.getSize());
    }

    public MovieResponse findById(Long id){
        Movie movie = getMovie(id);
        return mapper.toDto(movie);
    }

    public void deleteMovie(Long id){
        Movie movie = getMovie(id);
        repository.delete(movie);
    }

    public MovieResponse update(MovieRequest request, Long id){
        Movie movie = getMovie(id);
        movie.setTitle(request.title());
        movie.setDescription(request.description());
        movie.setGenre(Genre.fromApi(request.genre()));
        movie.setReleaseYear(request.releaseYear());
        movie.setRating(request.rating());
        return mapper.toDto(repository.save(movie));
    }

    private Movie getMovie(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }

}
