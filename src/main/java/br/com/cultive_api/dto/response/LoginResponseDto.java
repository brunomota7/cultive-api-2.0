package br.com.cultive_api.dto.response;

import java.util.Set;

public record LoginResponseDto(String accessToken, Long expiresIn, Set<String> roles) {
}
