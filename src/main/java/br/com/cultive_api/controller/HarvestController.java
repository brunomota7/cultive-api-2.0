package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateHarvestDto;
import br.com.cultive_api.dto.response.HarvestResponseDto;
import br.com.cultive_api.service.HarvestService;
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
@RequestMapping("/harvest")
@PreAuthorize("hasAuthority('SCOPE_FARMER') or hasAuthority('SCOPE_ADMIN')")
public class HarvestController {

    private HarvestService harvestService;

    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @PostMapping
    public ResponseEntity<Void> createHarvest(
            @RequestBody CreateHarvestDto dto,
            JwtAuthenticationToken token
    ) {
        harvestService.createHarvest(dto, token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/my")
    public ResponseEntity<Page<HarvestResponseDto>> getAllMyHarvest(
            @PageableDefault(page = 1, size = 5, sort = "name") Pageable pageable,
            JwtAuthenticationToken token
    ) {
       Page<HarvestResponseDto> listHarvest = harvestService.getAllMyHarvest(token, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listHarvest);
    }

    @PutMapping("/{harvestId}/update")
    public ResponseEntity<Void> updateInfosHarvest(
            @PathVariable Long harvestId,
            @RequestBody CreateHarvestDto dto
    ) {
        harvestService.harvestUpdateInfo(harvestId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{harvestId}/delete")
    public ResponseEntity<Void> deleteHarvest(
            @PathVariable Long harvestId
    ) {
        harvestService.deleteHarvest(harvestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
