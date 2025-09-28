package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreateHarvestDto;
import br.com.cultive_api.dto.response.HarvestResponseDto;
import br.com.cultive_api.entites.Harvest;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.exception.HarvestNotFoundException;
import br.com.cultive_api.exception.UserNotFoundException;
import br.com.cultive_api.mapper.HarvestMapper;
import br.com.cultive_api.repository.HarvestRepository;
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
public class HarvestService {

    private HarvestRepository harvestRepository;
    private UserRepository userRepository;

    public HarvestService(HarvestRepository harvestRepository,
                          UserRepository userRepository) {
        this.harvestRepository = harvestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createHarvest(CreateHarvestDto dto, JwtAuthenticationToken token) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Harvest newHarvest = HarvestMapper.toEntity(dto, user);

        harvestRepository.save(newHarvest);
    }

    public Page<HarvestResponseDto> getAllMyHarvest(JwtAuthenticationToken token, Pageable pageable) {
        UUID userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return harvestRepository.findAllByUserUserId(user.getUserId(), pageable)
                .map(HarvestMapper::toHarvestRes);
    }

    @Transactional
    public void harvestUpdateInfo(Long harvestId, CreateHarvestDto dto) {
        Harvest harvestUpdate = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found"));

        HarvestMapper.toUpdate(dto, harvestUpdate);
        harvestRepository.save(harvestUpdate);
    }

    @Transactional
    public void deleteHarvest(Long harvestId) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found"));

        harvestRepository.delete(harvest);
    }

}
