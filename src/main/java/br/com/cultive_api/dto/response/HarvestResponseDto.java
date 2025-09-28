package br.com.cultive_api.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record HarvestResponseDto(
        Long plantingId,
        LocalDate harvestDate,
        Double quantHarvestedKg,
        Double amountLodtKg,
        LocalDateTime lastUpdate,
        UUID userId,
        String username
) {
}
