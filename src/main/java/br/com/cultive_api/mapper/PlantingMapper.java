package br.com.cultive_api.mapper;

import br.com.cultive_api.dto.request.CreatePlantingDto;
import br.com.cultive_api.dto.response.PlantingResponseDto;
import br.com.cultive_api.entites.Planting;
import br.com.cultive_api.entites.User;

public class PlantingMapper {

    public static Planting toEntity(CreatePlantingDto dto, User user) {
        Planting newPlanting = new Planting();
        newPlanting.setCulture(dto.culture());
        newPlanting.setDatePlanting(dto.datePlanting());
        newPlanting.setAreaHectares(dto.areaHectares());
        newPlanting.setHarvested(false);
        newPlanting.setUser(user);

        return newPlanting;
    }

    public static PlantingResponseDto toPlantingRes(Planting planting) {
        var plantingId = planting.getPlantingId();
        var culture = planting.getCulture();
        var datePlanting = planting.getDatePlanting();
        var areaHectares = planting.getAreaHectares();
        var harvested = planting.isHarvested();
        var userId = planting.getUser().getUserId();
        var username = planting.getUser().getName();

        return new PlantingResponseDto(plantingId, culture, datePlanting,
                                      areaHectares, harvested, userId, username);
    }

    public static void toUpdate(CreatePlantingDto dto, Planting updatePlanting) {
        updatePlanting.setCulture(dto.culture());
        updatePlanting.setDatePlanting(dto.datePlanting());
        updatePlanting.setAreaHectares(dto.areaHectares());
    }

}
