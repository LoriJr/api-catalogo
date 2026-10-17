package com.viratech.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovieResponse(

        Long id,
        String title,
        String description,
        String genre,
        Integer releaseYear,
        BigDecimal rating,
        LocalDateTime createdAt
) {
}
