package com.example.onedaycar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Double balance;
}
