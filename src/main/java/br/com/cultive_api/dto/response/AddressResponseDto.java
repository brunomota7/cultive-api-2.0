package br.com.cultive_api.dto.response;

import br.com.cultive_api.entites.User;

import java.util.UUID;

public record AddressResponseDto(Long addressId,
                                 String street,
                                 String number,
                                 String city,
                                 String state,
                                 String zipCode,
                                 String country,
                                 UUID userId,
                                 String username) {
}
