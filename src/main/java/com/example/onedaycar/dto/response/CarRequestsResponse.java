package com.example.onedaycar.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarRequestsResponse {
    private List<CarRequestResponse> carRequests;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CarRequestResponse {
        private Long id;
        private String renterFirstName;
        private String renterLastName;
        private String ownerFirstName;
        private String ownerLastName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String carType;
        private String location;
        private Integer minYear;
        private Double priceForDay;
        private String status;
    }
}