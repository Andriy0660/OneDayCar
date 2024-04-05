package com.example.onedaycar.dto.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarsResponse {
    private List<CarResponse> cars;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CarResponse {
        private Long id;
        private String ownerFirstName;
        private String ownerLastName;
        private String ownerPhone;
        private String vendor;
        private String model;
        private String carType;
        private String location;
        private String description;
        private Integer year;
        private Double priceForDay;
        private Boolean isDisabled;
    }
}


