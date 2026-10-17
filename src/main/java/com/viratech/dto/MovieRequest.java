package com.viratech.dto;

import com.viratech.enums.Genre;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MovieRequest(

        @NotBlank
        @Size(max = 100, min = 3)
        String title,

        @NotBlank
        @Size(max = 300, min = 10)
        String description,

        @NotBlank
        String genre,

        @NotNull
        @Min(1900)
        Integer releaseYear,

        @NotNull
        @DecimalMax("10.0")
        @DecimalMin("0.0")
        BigDecimal rating
) {
}
