package com.example.onedaycar.controller;

import com.example.onedaycar.dto.request.UpdateProfileRequest;
import com.example.onedaycar.dto.response.UserInfoResponse;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        userService.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(UserInfoResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .balance(user.getBalance())
                .build());
    }
}
