package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateUserDto;
import br.com.cultive_api.dto.request.LoginRequestDto;
import br.com.cultive_api.dto.response.LoginResponseDto;
import br.com.cultive_api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto res = authService.login(dto);
        return ResponseEntity.ok(res);
    }

}
