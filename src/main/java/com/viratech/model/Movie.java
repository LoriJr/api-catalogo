package com.viratech.model;

import com.viratech.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(name = "rating", nullable = false)
    private BigDecimal rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist(){
        if(createdAt == null) createdAt = LocalDateTime.now();
    }


}
