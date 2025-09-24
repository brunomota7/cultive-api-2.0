package br.com.cultive_api.mapper;

import br.com.cultive_api.dto.request.AddAddressDto;
import br.com.cultive_api.dto.response.AddressResponseDto;
import br.com.cultive_api.entites.Address;
import br.com.cultive_api.entites.User;

public class AddressMapper {

    public static Address toEntity(AddAddressDto dto, User user) {
        Address addAddress = new Address();
        addAddress.setStreet(dto.street());
        addAddress.setNumber(dto.number());
        addAddress.setCity(dto.city());
        addAddress.setState(dto.state());
        addAddress.setZipCode(dto.zipCode());
        addAddress.setCountry(dto.country());
        addAddress.setUser(user);

        return addAddress;
    }

    public static AddressResponseDto toAddressRes(Address address) {
        var addressId = address.getAddressId();
        var street = address.getStreet();
        var number = address.getNumber();
        var city = address.getCity();
        var state = address.getState();
        var zipCode = address.getZipCode();
        var country = address.getCountry();
        var userId = address.getUser().getUserId();
        var username = address.getUser().getName();

        return new AddressResponseDto(addressId, street, number, city,
                state, zipCode, country, userId, username);
    }

    public static void toUpdate(AddAddressDto dto, Address addressUpdate) {
        addressUpdate.setStreet(dto.street());
        addressUpdate.setNumber(dto.number());
        addressUpdate.setCity(dto.city());
        addressUpdate.setState(dto.state());
        addressUpdate.setZipCode(dto.zipCode());
        addressUpdate.setCountry(dto.country());
    }

}
