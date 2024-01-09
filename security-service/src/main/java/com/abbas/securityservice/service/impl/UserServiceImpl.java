package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.dto.UserResponseDto;
import com.abbas.securityservice.entity.User;
import com.abbas.securityservice.exception.NotFoundException;
import com.abbas.securityservice.mapper.UserMapper;
import com.abbas.securityservice.repository.UserRepository;
import com.abbas.securityservice.service.AuthenticationService;
import com.abbas.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authService;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.debug("getting All Users");
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : userList) {
         userResponseDtoList.add(userMapper.modelToDto(user));
        }
        return userResponseDtoList;
    }

    @Override
    public UserResponseDto getUserById(Integer id) {
        log.debug("getting user by Id");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String email) {
        log.debug("getting user by username");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return userMapper.modelToDto(user);
    }

    @Override
    public UserResponseDto createUser(User user) {
        log.debug("creating user");

        User savedUser = userRepository.save(user);
        return userMapper.modelToDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Integer id, User updatedUser) {
        log.debug("updating user");
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
        log.debug("deleting user by Id");
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user is not present.");
        }
        userRepository.deleteById(id);
    }
}
