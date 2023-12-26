package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.controller.dto.UserResponseDto;
import com.abbas.securityservice.domain.entity.User;
import com.abbas.securityservice.exception.NotFoundException;
import com.abbas.securityservice.mapper.UserMapper;
import com.abbas.securityservice.repository.UserRepository;
import com.abbas.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authService;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = null;
        for (User user: userList) {
         userResponseDtoList.add(userMapper.modelToDto(user));
        }
        return userResponseDtoList;
    }

    @Override
    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserResponseDto createUser(User user) {
        User savedUser = userRepository.save(user);
        return userMapper.modelToDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Integer id, User updatedUser) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user is not present.");
        }
        updatedUser.setId(id);
        User savedUser = userRepository.save(updatedUser);
        authService.revokeAllUserTokens(updatedUser.getEmail());
        return userMapper.modelToDto(savedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user is not present.");
        }
        userRepository.deleteById(id);
    }
}
