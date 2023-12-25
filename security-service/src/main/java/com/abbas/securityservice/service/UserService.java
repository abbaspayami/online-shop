package com.abbas.securityservice.service;

import com.abbas.securityservice.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Integer id);
    Optional<User> getUserByUsername(String username);
    User createUser(User user);
    User updateUser(Integer id, User updatedUser);
    void deleteUser(Integer id);

}
