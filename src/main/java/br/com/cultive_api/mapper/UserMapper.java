package br.com.cultive_api.mapper;

import br.com.cultive_api.dto.response.UserResponseDto;
import br.com.cultive_api.entites.Role;
import br.com.cultive_api.entites.User;

import java.util.Set;

public class UserMapper {

    public static UserResponseDto toDTO(User user) {
        var userId = user.getUserId();
        var name = user.getName();
        var email = user.getEmail();
        Set<Role> roles = user.getRoles();

        var userRes = new UserResponseDto(userId, name, email, roles);

        return userRes;
    }

}
