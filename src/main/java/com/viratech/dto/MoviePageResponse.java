package com.viratech.dto;

import java.util.List;

public record MoviePageResponse(
        List<MovieResponse> content,
        long totalElements,
        long totalPages,
        long currentPage,
        long pageSize
) {
}
