package com.example.onedaycar.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookingsResponse {
    private List<BookingResponse> allBookings;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String vendor;
        private String model;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;
    }
}
