package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.controller.dto.UserDto;
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
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = null;
        for (User user: userList) {
         userDtoList.add(userMapper.modelToDto(user));
        }
        return userDtoList;
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserDto getUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserDto createUser(User user) {
        User savedUser = userRepository.save(user);
        return userMapper.modelToDto(savedUser);
    }

    @Override
    public UserDto updateUser(Integer id, User updatedUser) {
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
