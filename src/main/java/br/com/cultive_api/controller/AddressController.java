package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.AddAddressDto;
import br.com.cultive_api.dto.response.AddressResponseDto;
import br.com.cultive_api.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addNewAddress(@RequestBody AddAddressDto dto,
                                              JwtAuthenticationToken token) {
        addressService.addNewAddress(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getMyAddress(JwtAuthenticationToken token) {
        List<AddressResponseDto> listAddress =addressService.getMyAddress(token);
        return ResponseEntity.status(HttpStatus.OK).body(listAddress);
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<Void> updateAddress(@PathVariable Long addressId,
                                              @RequestBody AddAddressDto dto) {
        addressService.updateAddress(addressId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
