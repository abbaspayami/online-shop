package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.domain.entity.User;
import com.abbas.securityservice.exception.NotFoundException;
import com.abbas.securityservice.repository.UserRepository;
import com.abbas.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user is not present."));
        return user;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User updatedUser) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user is not present.");
        }
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user is not present.");
        }
        userRepository.deleteById(id);
    }
}
