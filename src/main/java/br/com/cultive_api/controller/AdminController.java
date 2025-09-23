package br.com.cultive_api.controller;

import br.com.cultive_api.dto.request.CreateUserAdminDto;
import br.com.cultive_api.dto.response.UserResponseDto;
import br.com.cultive_api.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        adminService.createUserAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/remove-admin/{userId}")
    public ResponseEntity<Void> removeAdminRole(@PathVariable UUID userId,
                                JwtAuthenticationToken token) {
        adminService.removeAdminRole(userId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> listUsers = adminService.getAllUsers();
        return ResponseEntity.ok(listUsers);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
