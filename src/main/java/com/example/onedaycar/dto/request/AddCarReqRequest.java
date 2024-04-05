package com.example.onedaycar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCarReqRequest {

    @NotBlank
    private String carType;

    @NotNull
    @Range(min = 1980, max = 2024)
    private Integer minYear;

    @NotBlank
    private String location;

    @NotNull
    private Double priceForDay;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}