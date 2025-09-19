package br.com.cultive_api.dto.request;

public record CreateUserDto(String name, String email, String password, String role) {
}
