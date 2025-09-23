package br.com.cultive_api.service;

import br.com.cultive_api.dto.response.UserResponseDto;
import br.com.cultive_api.entites.User;
import br.com.cultive_api.exception.UserNotFoundException;
import br.com.cultive_api.mapper.UserMapper;
import br.com.cultive_api.repository.UserRepository;
import br.com.cultive_api.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto getAllMyInfos(JwtAuthenticationToken token) {
        var userId = AppUtils.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserMapper.toDTO(user);
    }

}
