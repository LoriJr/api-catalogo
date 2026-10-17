package com.viratech.dto;

import java.math.BigDecimal;

public record MovieFilterRequest(
        String genre,
        BigDecimal minRating,
        Integer releaseYear
) {
}
