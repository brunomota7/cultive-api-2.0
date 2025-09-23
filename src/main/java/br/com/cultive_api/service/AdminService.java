package br.com.cultive_api.service;

import br.com.cultive_api.dto.request.CreateUserAdminDto;
import br.com.cultive_api.dto.response.UserResponseDto;
import br.com.cultive_api.entites.Role;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.exception.CannotDeleteAdminException;
import br.com.cultive_api.exception.CannotRemoveAdminRoleException;
import br.com.cultive_api.exception.UserNotFoundException;
import br.com.cultive_api.mapper.UserMapper;
import br.com.cultive_api.repository.RoleRepository;
import br.com.cultive_api.repository.UserRepository;
import br.com.cultive_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
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
        log.info("Starting creation process for a new admin user with email: {}", dto.email());

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        if (roleAdmin == null) {
            log.info("Admin role not found, creating it.");
            roleAdmin = new Role();
            roleAdmin.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdmin);
        }

        var existingUser = userRepository.findByEmail(dto.email());
        if (existingUser.isPresent()) {
            log.warn("Attempt to create an admin user with an existing email: {}", dto.email());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exists");
        }

        var user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(roleAdmin));

        userRepository.save(user);
        log.info("Admin user created successfully with ID: {}", user.getUserId());
    }

    @Transactional
    public void removeAdminRole(UUID targetUserId, JwtAuthenticationToken token) {
        log.info("Starting process to remove admin role for user ID: {}", targetUserId);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> {
                    log.error("User with ID '{}' not found", targetUserId);
                    return new UserNotFoundException("User not found");
                });

        UUID currentUserId = AppUtils.getUserIdFromToken(token);
        if (currentUserId.equals(targetUser.getUserId())) {
            log.warn("Attempted to remove own admin role by user ID: {}", currentUserId);
            throw new CannotRemoveAdminRoleException("You cannot remove your own admin role");
        }

        Role adminRole = roleRepository.findByName(Role.Values.ADMIN.name());
        if (!targetUser.getRoles().contains(adminRole)) {
            log.warn("Attempted to remove admin role from a user who is not an admin. User ID: {}", targetUserId);
            throw new CannotRemoveAdminRoleException("User is not an admin");
        }

        long totalAdmins = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(adminRole))
                .count();
        if (totalAdmins <= 1) {
            log.warn("Attempted to remove the last admin role. User ID: {}", targetUserId);
            throw new CannotRemoveAdminRoleException("Cannot remove the last admin");
        }

        targetUser.getRoles().remove(adminRole);
        userRepository.save(targetUser);
        log.info("Admin role successfully removed from user ID: {}", targetUserId);
    }

    public List<UserResponseDto> getAllUsers() {
        log.info("Starting request to list all users");
        List<UserResponseDto> users = userRepository
                .findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
        log.info("Found {} users in the database", users.size());
        return users;
    }

    @Transactional
    public void deleteUser(UUID userId) {
        log.warn("Starting user deletion process for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID '{}' not found for deletion", userId);
                    return new UserNotFoundException("User not found");
                });

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name()));

        if (isAdmin) {
            log.warn("Attempted to delete an admin user. User ID: {}", userId);
            throw new CannotDeleteAdminException("Admins cannot be deleted");
        }

        userRepository.delete(user);
        log.warn("User with ID '{}' successfully deleted", userId);
    }
}