package com.viratech.controller;

import com.viratech.dto.MovieFilterRequest;
import com.viratech.dto.MoviePageResponse;
import com.viratech.dto.MovieRequest;
import com.viratech.dto.MovieResponse;
import com.viratech.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @PostMapping
    public ResponseEntity<MovieResponse> saveMovie(@Valid @RequestBody MovieRequest request){
        MovieResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<MoviePageResponse> listAllMovies(
            @ModelAttribute MovieFilterRequest filter, Pageable pageable
            ){
        return ResponseEntity.ok().body(service.listAllMovies(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> find(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest request
    ){
        MovieResponse response = service.update(request, id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

}
