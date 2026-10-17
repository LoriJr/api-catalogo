package com.viratech.mapper;

import com.viratech.dto.MovieRequest;
import com.viratech.dto.MovieResponse;
import com.viratech.enums.Genre;
import com.viratech.model.Movie;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MovieMapper {

    public MovieResponse toDto(Movie movie){

        LocalDateTime createdAt = movie.getCreatedAt() != null
                ? movie.getCreatedAt()
                : LocalDateTime.now();

        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getGenre().toApi(),
                movie.getReleaseYear(),
                movie.getRating(),
                createdAt
        );
    }

    public Movie toEntity(MovieRequest request){

        return Movie.builder()
                .title(request.title())
                .description(request.description())
                .genre(Genre.fromApi(request.genre()))
                .releaseYear(request.releaseYear())
                .rating(request.rating())
                .build();
    }

    public List<MovieResponse> responseList(List<Movie> movie){
        return movie.stream().map(this::toDto).toList();
    }
}
