package br.com.cultive_api.dto.request;

import br.com.cultive_api.entites.User;

import java.time.LocalTime;

public record CreatePlantingDto(String culture,
                                LocalTime datePlanting,
                                double areaHectares,
                                User user) {
}
