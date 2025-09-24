package br.com.cultive_api.dto.request;

import br.com.cultive_api.entites.User;

public record AddAddressDto(String street, String number, String city, String state, String zipCode, String country, User user) {
}
