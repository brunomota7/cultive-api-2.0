package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreatePlantingDto;
import br.com.cultive_api.dto.response.PlantingResponseDto;
import br.com.cultive_api.entites.Planting;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.exception.PlantingNotFoundException;
import br.com.cultive_api.exception.UserNotFoundException;
import br.com.cultive_api.mapper.PlantingMapper;
import br.com.cultive_api.repository.PlantingRepository;
import br.com.cultive_api.repository.UserRepository;
import br.com.cultive_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PlantingService {

    private PlantingRepository plantingRepository;
    private UserRepository userRepository;

    public PlantingService(PlantingRepository plantingRepository,
                           UserRepository userRepository) {
        this.plantingRepository = plantingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createPlanting(CreatePlantingDto dto, JwtAuthenticationToken token) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Planting newPlanting = PlantingMapper.toEntity(dto, user);

        plantingRepository.save(newPlanting);
    }

    public Page<PlantingResponseDto> getAllMyPlanting(JwtAuthenticationToken token, Pageable pageable) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return plantingRepository.findAllByUserUserId(user.getUserId(), pageable)
                .map(PlantingMapper::toPlantingRes);
    }

    public Page<PlantingResponseDto> getAllPlantingHarvestedTrue(JwtAuthenticationToken token, Pageable pageable) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return plantingRepository.findByUserUserIdAndHarvestedTrue(user.getUserId(), pageable)
                .map(PlantingMapper::toPlantingRes);
    }

    public Page<PlantingResponseDto> getAllPlantingHarvetedFalse(JwtAuthenticationToken token, Pageable pageable) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return plantingRepository.findByUserUserIdAndHarvestedFalse(user.getUserId(), pageable)
                .map(PlantingMapper::toPlantingRes);
    }

    @Transactional
    public void changeHarvestedStatus(Long plantingId, boolean harvested) {
        Planting planting = plantingRepository.findById(plantingId)
                .orElseThrow(() -> new PlantingNotFoundException("Planting not found"));
        planting.setHarvested(harvested);
        plantingRepository.save(planting);
    }

    @Transactional
    public void updateInfosPlanting(Long plantingId, CreatePlantingDto dto) {
        Planting updatePlanting = plantingRepository.findById(plantingId)
                .orElseThrow(() -> new PlantingNotFoundException("Planting not found"));

        PlantingMapper.toUpdate(dto, updatePlanting);
        plantingRepository.save(updatePlanting);
    }

    @Transactional
    public void deletePlanting(Long plantingId) {
        Planting planting = plantingRepository.findById(plantingId)
                .orElseThrow(() -> new PlantingNotFoundException("Planting not found"));

        plantingRepository.delete(planting);
    }

}
