package com.abbas.securityservice.controller;

import com.abbas.securityservice.controller.dto.UserDto;
import com.abbas.securityservice.domain.entity.User;
import com.abbas.securityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        UserDto newUser = userService.createUser(user);
        return ResponseEntity.created(URI.create("/admin/users/" + newUser.getId())).body(newUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        UserDto savedUser = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }



}
