package com.example.onedaycar.controller;

import com.example.onedaycar.dto.request.SignInRequest;
import com.example.onedaycar.dto.request.SignUpRequest;
import com.example.onedaycar.dto.response.SignInResponse;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.exception.UnauthorizedException;
import com.example.onedaycar.service.AuthenticationService;
import com.example.onedaycar.service.JwtService;
import com.example.onedaycar.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        authenticationService.signUp(request);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @GetMapping
    public ResponseEntity<Void> isTokenValid(@RequestParam String token) {
        String userEmail;
        try {
            userEmail = jwtService.extractUsername(token);
        } catch (JwtException e) {
            throw new UnauthorizedException();
        }
        User user = userService.findByEmail(userEmail);
        if (!jwtService.isTokenValid(token, user)) {
            throw new UnauthorizedException();
        }
        return ResponseEntity.ok().build();
    }
}
