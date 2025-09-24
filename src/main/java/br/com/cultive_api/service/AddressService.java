package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.AddAddressDto;
import br.com.cultive_api.dto.response.AddressResponseDto;
import br.com.cultive_api.entites.Address;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.exception.AddressNotFoundException;
import br.com.cultive_api.exception.UserNotFoundException;
import br.com.cultive_api.mapper.AddressMapper;
import br.com.cultive_api.repository.AddressRepository;
import br.com.cultive_api.repository.UserRepository;
import br.com.cultive_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressService {

    private AddressRepository addressRepository;
    private UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addNewAddress(AddAddressDto dto, JwtAuthenticationToken token) {
        log.info("Starting the process of adding a new address for the user");
        User user = userRepository.findById(AppUtils.getUserIdFromToken(token))
                .orElseThrow(() -> {
                    log.error("User not found during address creation");
                    return new UserNotFoundException("User not found");
                });

        Address address = AddressMapper.toEntity(dto, user);

        addressRepository.save(address);
        log.info("New address added successfully for user ID: {}", user.getUserId());
    }

    public List<AddressResponseDto> getMyAddress(JwtAuthenticationToken token) {
        log.info("Starting the search for addresses for the user");
        User user = userRepository.findById(AppUtils.getUserIdFromToken(token))
                .orElseThrow(() -> {
                    log.error("User not found while searching for addresses");
                    return new UserNotFoundException("User not found");
                });

        List<AddressResponseDto> addresses = addressRepository.findByUserUserId(user.getUserId())
                .stream()
                .map(AddressMapper::toAddressRes)
                .toList();

        log.info("Found {} addresses for user ID: {}", addresses.size(), user.getUserId());
        return addresses;
    }

    @Transactional
    public void updateAddress(Long addressId, AddAddressDto dto) {
        log.info("Starting the process of updating address with ID: {}", addressId);
        Address addressUpdate = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address with ID '{}' not found for update", addressId);
                    return new AddressNotFoundException("Address not found");
                });

        AddressMapper.toUpdate(dto, addressUpdate);
        addressRepository.save(addressUpdate);
        log.info("Address with ID '{}' successfully updated", addressId);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        log.warn("Starting the process of deleting address with ID: {}", addressId);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address with ID '{}' not found for deletion", addressId);
                    return new AddressNotFoundException("Address not found");
                });

        addressRepository.delete(address);
        log.warn("Address with ID '{}' successfully deleted", addressId);
    }
}