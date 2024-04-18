package com.example.onedaycar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCarRequest {
    @NotBlank
    private String vendor;

    @NotBlank
    private String model;

    @NotBlank
    private String carType;

    @NotNull
    @Range(min = 1980, max = 2024)
    private Integer year;

    @NotBlank
    private String location;

    @NotBlank
    private String description;

    @NotNull
    private Double priceForDay;
}
