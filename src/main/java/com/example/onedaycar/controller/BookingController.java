package com.example.onedaycar.controller;

import com.example.onedaycar.dto.response.BookingsResponse;
import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.entity.Car;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.exception.ConflictException;
import com.example.onedaycar.service.BookingService;
import com.example.onedaycar.service.CarService;
import com.example.onedaycar.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final CarService carService;

    @GetMapping
    public ResponseEntity<BookingsResponse> getAllBookings(@RequestParam Boolean byOwner) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(bookingService.getAllBookings(user.getId(), byOwner));
    }
    @GetMapping("/car")
    public ResponseEntity<BookingsResponse> getAllBookingsForCar(@RequestParam Long carId){
        return ResponseEntity.ok(bookingService.getAllBookingsForCar(carId));
    }

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestParam(name = "carId") Long carId,
                                              @RequestParam(name = "startDate") LocalDate startDate,
                                              @RequestParam(name = "endDate") LocalDate endDate) {

        User renter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Car car = carService.findById(carId);

        if (!carService.isCarAvailable(car, startDate, endDate))
            throw new ConflictException("Somebody rented car(");

        bookingService.save(Booking.builder()
                .renter(renter)
                .car(car)
                .ownerId(car.getOwnerId())
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.CREATED.toString())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    // лише до букінгів на його машини
    @PatchMapping("/setStatus")
    public ResponseEntity<Void> setStatus(@RequestParam(name = "carId") Long carId,
                                          @RequestParam(name = "status") Status status) {
        Booking booking = bookingService.findById(carId);
        booking.setStatus(status.toString());
        bookingService.save(booking);
        return ResponseEntity.noContent().build();
    }


}
