package com.example.onedaycar.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBookingRequest {
    @NotNull
    private Long carId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
