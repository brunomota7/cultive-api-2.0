package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateUserAdminDto;
import br.com.cultive_api.dto.response.UserResponseDto;
import br.com.cultive_api.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("admin")
@PreAuthorize("hasRole('SCOPE_ADMIN')")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Void> addNewAdmin(@RequestBody CreateUserAdminDto dto) {
        log.info("POST /admin/register-admin - Starting new admin registration request");
        adminService.createUserAdmin(dto);
        log.info("POST /admin/register-admin - New admin registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/remove-admin/{userId}")
    public ResponseEntity<Void> removeAdminRole(@PathVariable UUID userId,
                                                JwtAuthenticationToken token) {
        log.info("DELETE /admin/remove-admin/{} - Starting request to remove admin role", userId);
        adminService.removeAdminRole(userId, token);
        log.info("DELETE /admin/remove-admin/{} - Admin role removed successfully", userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("GET /admin/user - Starting request to list all users");
        List<UserResponseDto> listUsers = adminService.getAllUsers();
        log.info("GET /admin/user - Found {} users", listUsers.size());
        return ResponseEntity.ok(listUsers);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID userId) {
        log.warn("DELETE /admin/user/{} - Starting request to delete a user", userId);
        adminService.deleteUser(userId);
        log.warn("DELETE /admin/user/{} - User deleted successfully", userId);
        return ResponseEntity.ok().build();
    }

}