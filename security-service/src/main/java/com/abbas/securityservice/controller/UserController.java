package com.abbas.securityservice.controller;

import com.abbas.securityservice.dto.UserRequestDto;
import com.abbas.securityservice.dto.UserResponseDto;
import com.abbas.securityservice.entity.User;
import com.abbas.securityservice.mapper.UserMapper;
import com.abbas.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@SuppressWarnings({"unused"})
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        log.debug("UserController: getting all users");
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        log.debug("UserController: getting user by id");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto request) {
        log.debug("UserController: creating user");
        User user = userMapper.dtoToModel(request);
        UserResponseDto newUser = userService.createUser(user);
        return ResponseEntity.created(URI.create("/admin/users/" + newUser.id())).body(newUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRequestDto request) {
        log.debug("UserController: updating user");
        User user = userMapper.dtoToModel(request);
        UserResponseDto savedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.debug("UserController: deleting user by id");
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }



}
