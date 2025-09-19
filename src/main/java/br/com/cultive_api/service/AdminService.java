package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreateUserAdminDto;
import br.com.cultive_api.entites.Role;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.repository.RoleRepository;
import br.com.cultive_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminService(RoleRepository roleRepository,
                        UserRepository userRepository,
                        BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUserAdmin(CreateUserAdminDto dto) {
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdmin);
        }

        var existingUser = userRepository.findByEmail(dto.email());
        if (existingUser.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exists");

        var user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(roleAdmin));

        userRepository.save(user);
    }

}
