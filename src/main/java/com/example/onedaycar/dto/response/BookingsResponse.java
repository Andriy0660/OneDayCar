package com.example.onedaycar.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookingsResponse {
    private List<BookingResponse> bookings;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingResponse {
        private Long id;
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String vendor;
        private String model;
        private Double fullPrice;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;
    }
}
