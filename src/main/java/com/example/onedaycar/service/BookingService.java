package com.example.onedaycar.service;

import com.example.onedaycar.dto.response.BookingsResponse;
import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarService carService;

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Booking with such id does not exist"));
    }

    public BookingsResponse getAllBookings(Long id) {
        List<Booking> bookings = bookingRepository.findAllByRenterId(id);

        return new BookingsResponse(bookings
                .stream()
                .map(b -> BookingsResponse.BookingResponse.builder()
                        .id(b.getId())
                        .userId(b.getOwnerId())
                        .firstName(b.getOwner().getFirstName())
                        .lastName(b.getOwner().getLastName())
                        .email(b.getOwner().getEmail())
                        .phone(b.getOwner().getPhone())
                        .vendor(b.getCar().getVendor())
                        .model(b.getCar().getModel())
                        .fullPrice((ChronoUnit.DAYS.between(b.getStartDate(), b.getEndDate()) + 1) * b.getCar().getPriceForDay())
                        .startDate(b.getStartDate())
                        .endDate(b.getEndDate())
                        .status(b.getStatus())
                        .build())
                .sorted(Comparator.comparing(BookingsResponse.BookingResponse::getStartDate).reversed())
                .toList());
    }

    public BookingsResponse getAllBookingsForCar(Long carId) {
        return new BookingsResponse(carService.findById(carId).getBookings().stream().map((b) ->
                        BookingsResponse.BookingResponse.builder()
                                .firstName(b.getRenter().getFirstName())
                                .lastName(b.getRenter().getLastName())
                                .email(b.getRenter().getEmail())
                                .phone(b.getRenter().getPhone())
                                .status(b.getStatus())
                                .fullPrice((ChronoUnit.DAYS.between(b.getStartDate(), b.getEndDate()) + 1) * b.getCar().getPriceForDay())
                                .startDate(b.getStartDate())
                                .endDate(b.getEndDate())
                                .id(b.getId())
                                .build()
                )
                .sorted(Comparator.comparing(BookingsResponse.BookingResponse::getStartDate).reversed())
                .toList());
    }

    public List<Booking> getAllByStatus(String status) {
        return bookingRepository.findAllByStatusIs(status);
    }
}
