package br.com.cultive_api.dto.request;

import br.com.cultive_api.entites.User;

import java.time.LocalDate;

public record CreateHarvestDto(
        LocalDate harvestDate,
        Double quantHarvestedKg,
        Double amountLodtKg
) {
}
