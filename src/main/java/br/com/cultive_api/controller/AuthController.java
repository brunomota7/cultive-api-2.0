package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateUserDto;
import br.com.cultive_api.dto.request.LoginRequestDto;
import br.com.cultive_api.dto.response.LoginResponseDto;
import br.com.cultive_api.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CreateUserDto dto) {
        log.info("POST /auth/register - Starting user registration request for email: {}", dto.email());
        authService.register(dto);
        log.info("POST /auth/register - User registered successfully with email: {}", dto.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        log.info("POST /auth/login - Starting login request for email: {}", dto.email());
        LoginResponseDto res = authService.login(dto);
        log.info("POST /auth/login - User logged in successfully. Token generated.");
        return ResponseEntity.ok(res);
    }
}