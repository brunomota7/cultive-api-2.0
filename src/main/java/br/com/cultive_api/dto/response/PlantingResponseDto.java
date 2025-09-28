package br.com.cultive_api.dto.response;

import java.time.LocalTime;
import java.util.UUID;

public record PlantingResponseDto(
        Long plantingId,
        String culture,
        LocalTime datePlanting,
        double areaHectares,
        boolean harvested,
        UUID userId,
        String username
) {
}
