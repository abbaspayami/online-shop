package com.abbas.securityservice.service;

import com.abbas.securityservice.controller.dto.UserResponseDto;
import com.abbas.securityservice.domain.entity.User;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Integer id);
    UserResponseDto getUserByUsername(String email);
    UserResponseDto createUser(User user);
    UserResponseDto updateUser(Integer id, User updatedUser);
    void deleteUser(Integer id);

}
