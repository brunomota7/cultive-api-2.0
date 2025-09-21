package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreateUserDto;
import br.com.cultive_api.dto.request.LoginRequestDto;
import br.com.cultive_api.dto.response.LoginResponseDto;
import br.com.cultive_api.entites.Role;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.repository.RoleRepository;
import br.com.cultive_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

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
       var userFromDb = userRepository.findByEmail(dto.email());
       if (userFromDb.isPresent())
           throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists.");

       Role userRole = roleRepository.findByName(dto.role());
       if (userRole == null)
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found");

       if (userRole.getRoleId().equals(Role.Values.ADMIN.getRoleId()))
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role for registration");

       User user = new User();
       user.setName(dto.name());
       user.setEmail(dto.email());
       user.setPassword(passwordEncoder.encode(dto.password()));
       user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        var user = userRepository.findByEmail(dto.email());
        if (user.isEmpty() || !user.get().isLoginCorrect(dto, passwordEncoder))
            throw new BadCredentialsException("User or password is invalid!");

        var expiresIn = 172800L;
        var token = jwtTokenService.generateToken(user.get(), expiresIn);

        Set<String> roles = user.get().getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new LoginResponseDto(token, expiresIn, roles);
    }

}
