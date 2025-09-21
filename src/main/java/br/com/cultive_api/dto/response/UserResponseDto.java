package br.com.cultive_api.dto.response;

import br.com.cultive_api.entites.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponseDto(UUID userId, String name, String email, Set<Role> roles) {
}
