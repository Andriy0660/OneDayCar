package com.example.onedaycar.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}