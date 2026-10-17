package com.viratech.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer codeStatus,
        String message,
        LocalDateTime timestamp,
        String path
) {
}
