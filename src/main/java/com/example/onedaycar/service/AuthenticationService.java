package com.example.onedaycar.service;

import com.example.onedaycar.dto.request.SignInRequest;
import com.example.onedaycar.dto.request.SignUpRequest;
import com.example.onedaycar.dto.response.SignInResponse;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void signUp(SignUpRequest request) {
        String email = request.getEmail();
        String phone = request.getPhone();

        if (userService.existsUserByEmail(email)) {
            throw new BadRequestException("The email is already used");
        }
        if (userService.existsUserByPhone(phone)) {
            throw new BadRequestException("The phone is already used");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .balance(0.)
                .build();
        userService.save(user);
    }

    public SignInResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new UnauthorizedException("Username or password is wrong");
        }

        User user = userService.findByEmail(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return new SignInResponse(jwtToken, user.getId());
    }
}
