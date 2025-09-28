package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreatePlantingDto;
import br.com.cultive_api.dto.response.PlantingResponseDto;
import br.com.cultive_api.service.PlantingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/planting")
@PreAuthorize("hasAuthority('SCOPE_FARMER') or hasAuthority('SCOPE_ADMIN')")
public class PlantingController {

    private PlantingService plantingService;

    public PlantingController(PlantingService plantingService) {
        this.plantingService = plantingService;
    }

    @PostMapping
    public ResponseEntity<Void> createPlanting(@RequestBody CreatePlantingDto dto,
                                         JwtAuthenticationToken token) {
        plantingService.createPlanting(dto, token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/my")
    public ResponseEntity<Page<PlantingResponseDto>> getAllMyPlanting(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable,
            JwtAuthenticationToken token
    ) {
        Page<PlantingResponseDto> listPlanting = plantingService.getAllMyPlanting(token, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listPlanting);
    }

    @GetMapping("/harvested")
    public ResponseEntity<Page<PlantingResponseDto>> getAllPlantingHarvestedTrue(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable,
            JwtAuthenticationToken token
    ) {
        Page<PlantingResponseDto> listHarvestedTrue = plantingService
                .getAllPlantingHarvestedTrue(token, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listHarvestedTrue);
    }

    @GetMapping("/no-harvested")
    public ResponseEntity<Page<PlantingResponseDto>> getAllPlantingHarvestedFalse(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable,
            JwtAuthenticationToken token
    ) {
        Page<PlantingResponseDto> listHarvestedFalse = plantingService
                .getAllPlantingHarvetedFalse(token, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listHarvestedFalse);
    }

    @PatchMapping("/{plantingId}/harvest")
    public ResponseEntity<Void> changeHarvestedStatus(
            @PathVariable Long plantingId,
            @RequestParam boolean harvested
    ) {
        plantingService.changeHarvestedStatus(plantingId, harvested);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{plantingId}/update")
    public ResponseEntity<Void> updateInfosPlanting(
            @PathVariable Long plantingId,
            @RequestBody CreatePlantingDto dto
    ) {
        plantingService.updateInfosPlanting(plantingId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{plantingId}/delete")
    public ResponseEntity<Void> deletePlanting(
            @PathVariable Long plantingId
    ) {
        plantingService.deletePlanting(plantingId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
