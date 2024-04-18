package com.example.onedaycar.controller;

import com.example.onedaycar.dto.request.AddBookingRequest;
import com.example.onedaycar.dto.response.BookingsResponse;
import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.entity.Car;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.exception.ConflictException;
import com.example.onedaycar.service.BookingService;
import com.example.onedaycar.service.CarService;
import com.example.onedaycar.util.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final CarService carService;

    @GetMapping
    public ResponseEntity<BookingsResponse> getAllBookings() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(bookingService.getAllBookings(user.getId()));
    }

    @GetMapping("/car")
    public ResponseEntity<BookingsResponse> getAllBookingsForCar(@RequestParam Long carId) {
        return ResponseEntity.ok(bookingService.getAllBookingsForCar(carId));
    }

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody @Valid AddBookingRequest request) {

        User renter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Car car = carService.findById(request.getCarId());

        if (!carService.isCarAvailable(car, request.getStartDate(), request.getEndDate())
                || car.getIsDisabled())
            throw new ConflictException(
                    "Car is not available at the moment :-( Reload page and look for actual cars");

        bookingService.save(Booking.builder()
                .renter(renter)
                .car(car)
                .owner(car.getOwner())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(Status.CREATED.toString())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PatchMapping("/setStatus")
    public ResponseEntity<Void> setStatus(@RequestParam(name = "bookingId") Long bookingId,
                                          @RequestParam(name = "status") Status status) {
        Booking booking = bookingService.findById(bookingId);
        booking.setStatus(status.toString());
        bookingService.save(booking);
        return ResponseEntity.noContent().build();
    }

}
