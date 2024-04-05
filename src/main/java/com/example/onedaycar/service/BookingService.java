package com.example.onedaycar.service;

import com.example.onedaycar.dto.response.BookingsResponse;
import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public BookingsResponse getAllBookings(Long id, Boolean byOwner) {
        List<Booking> bookings;
        if (byOwner)
            bookings = bookingRepository.findAllByOwnerId(id);
        else
            bookings = bookingRepository.findAllByRenterId(id);

        return new BookingsResponse(bookings
                .stream()
                .map(b -> BookingsResponse.BookingResponse.builder()
                        .id(b.getId())
                        .firstName(b.getRenter().getFirstName())
                        .lastName(b.getRenter().getLastName())
                        .vendor(b.getCar().getVendor())
                        .model(b.getCar().getModel())
                        .startDate(b.getStartDate())
                        .endDate(b.getEndDate())
                        .status(b.getStatus())
                        .build())
                .toList());
    }

    public BookingsResponse getAllBookingsForCar(Long carId) {
        return new BookingsResponse(carService.findById(carId).getBookings().stream().map((c) ->
                BookingsResponse.BookingResponse.builder()
                        .firstName(c.getRenter().getFirstName())
                        .lastName(c.getRenter().getLastName())
                        .status(c.getStatus())
                        .startDate(c.getStartDate())
                        .endDate(c.getEndDate())
                        .id(c.getId())
                        .build()
        ).toList());
    }

    public List<Booking> getAllByStatus(String status) {
        return bookingRepository.findAllByStatusIs(status);
    }
}
