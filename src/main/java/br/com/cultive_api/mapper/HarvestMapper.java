package br.com.cultive_api.mapper;

import br.com.cultive_api.dto.request.CreateHarvestDto;
import br.com.cultive_api.dto.response.HarvestResponseDto;
import br.com.cultive_api.dto.response.PlantingResponseDto;
import br.com.cultive_api.entites.Harvest;
import br.com.cultive_api.entites.User;

import java.time.LocalDateTime;

public class HarvestMapper {

    public static Harvest toEntity(CreateHarvestDto dto, User user) {
        Harvest newHarvest = new Harvest();
        newHarvest.setHarvestDate(dto.harvestDate());
        newHarvest.setQuantHarvestedKg(dto.quantHarvestedKg());
        newHarvest.setAmountLodtKg(dto.amountLodtKg());

        return newHarvest;
    }

    public static HarvestResponseDto toHarvestRes(Harvest harvest) {
        var harvestId = harvest.getHarvestId();
        var harvestDate = harvest.getHarvestDate();
        var quantHarvestedKg = harvest.getQuantHarvestedKg();
        var amoutLodtKg = harvest.getAmountLodtKg();
        var lastUpdate = harvest.getLastUpdate();
        var userId = harvest.getUser().getUserId();
        var username = harvest.getUser().getName();

        return new HarvestResponseDto(harvestId, harvestDate, quantHarvestedKg,
                                     amoutLodtKg, lastUpdate, userId, username);
    }

    public static void toUpdate(CreateHarvestDto dto, Harvest updateHarvest) {
        updateHarvest.setHarvestDate(dto.harvestDate());
        updateHarvest.setQuantHarvestedKg(dto.quantHarvestedKg());
        updateHarvest.setAmountLodtKg(dto.amountLodtKg());
        updateHarvest.setLastUpdate(LocalDateTime.now());
    }

}
