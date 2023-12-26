package com.abbas.securityservice.service;

import com.abbas.securityservice.controller.dto.UserDto;
import com.abbas.securityservice.domain.entity.User;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto getUserById(Integer id);
    UserDto getUserByUsername(String email);
    UserDto createUser(User user);
    UserDto updateUser(Integer id, User updatedUser);
    void deleteUser(Integer id);

}
