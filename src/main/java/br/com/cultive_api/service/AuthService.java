package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreateUserDto;
import br.com.cultive_api.dto.request.LoginRequestDto;
import br.com.cultive_api.dto.response.LoginResponseDto;
import br.com.cultive_api.entites.Role;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.repository.RoleRepository;
import br.com.cultive_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       JwtTokenService jwtTokenService,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public void register(CreateUserDto dto) {
        log.info("Starting new user registration process for email: {}", dto.email());

        var userFromDb = userRepository.findByEmail(dto.email());
        if (userFromDb.isPresent()) {
            log.warn("Attempt to register a user with an existing email: {}", dto.email());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists.");
        }

        Role userRole = roleRepository.findByName(dto.role());
        if (userRole == null) {
            log.error("Role '{}' not found for registration", dto.role());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found");
        }

        if (userRole.getRoleId().equals(Role.Values.ADMIN.getRoleId())) {
            log.warn("Attempt to register with an invalid role: {}", dto.role());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role for registration");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
        log.info("User registered successfully with email: {}", dto.email());
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        log.info("Attempting to log in with email: {}", dto.email());

        var user = userRepository.findByEmail(dto.email());
        if (user.isEmpty() || !user.get().isLoginCorrect(dto, passwordEncoder)) {
            log.warn("Login failed for email: {}. Invalid credentials.", dto.email());
            throw new BadCredentialsException("User or password is invalid!");
        }

        var expiresIn = 172800L;
        var token = jwtTokenService.generateToken(user.get(), expiresIn);
        log.info("Login successful for user ID: {}", user.get().getUserId());

        Set<String> roles = user.get().getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new LoginResponseDto(token, expiresIn, roles);
    }
}