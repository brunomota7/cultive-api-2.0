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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    public void createUserAdmin(CreateUserAdminDto dto, JwtAuthenticationToken token) {
        var scopeAdmin = AppUtils.getScopeUser(token);
        AppUtils.isAdmin(scopeAdmin);

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

    @Transactional
    public void removeAdminRole(UUID targetUserId, JwtAuthenticationToken token) {
        var scopeAdmin = AppUtils.getScopeUser(token);
        AppUtils.isAdmin(scopeAdmin);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UUID currentUserId = AppUtils.getUserIdFromToken(token);
        if (currentUserId.equals(targetUser))
            throw new CannotRemoveAdminRoleException("You cannot remove your own admin role");

        Role adminRole = roleRepository.findByName(Role.Values.ADMIN.name());
        if (!targetUser.getRoles().contains(adminRole))
            throw new CannotRemoveAdminRoleException("User is not an admin");

        long totalAdmins = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(adminRole))
                .count();
        if (totalAdmins <= 1)
            throw new CannotRemoveAdminRoleException("Cannot remove the last admin");

        targetUser.getRoles().remove(adminRole);
        userRepository.save(targetUser);
    }

    public List<UserResponseDto> getAllUsers(JwtAuthenticationToken token) {
        var scopeAdmin = AppUtils.getScopeUser(token);
        AppUtils.isAdmin(scopeAdmin);

        return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public void deleteUser(UUID userId, JwtAuthenticationToken token) {
        var scopeAdmin = AppUtils.getScopeUser(token);
        AppUtils.isAdmin(scopeAdmin);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name()));

        if (isAdmin)
            throw new CannotDeleteAdminException("Admins cannot be deleted");

        userRepository.delete(user);
    }

}
