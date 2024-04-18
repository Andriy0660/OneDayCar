package com.example.onedaycar.controller;

import com.example.onedaycar.dto.request.AddCarRequest;
import com.example.onedaycar.dto.request.GetCarsRequest;
import com.example.onedaycar.dto.response.CarsResponse;
import com.example.onedaycar.entity.Car;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody @Valid AddCarRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Car car = Car.builder()
                .owner(user)
                .vendor(request.getVendor())
                .model(request.getModel())
                .carType(request.getCarType())
                .description(request.getDescription())
                .location(request.getLocation())
                .priceForDay(request.getPriceForDay())
                .year(request.getYear())
                .isDisabled(false)
                .build();
        carService.save(car);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PostMapping("/available")
    public ResponseEntity<CarsResponse> getAvailableCars(@RequestBody @Valid GetCarsRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(carService.getAllAvailableCars(user.getId(), request));
    }

    @GetMapping("/owned")
    public ResponseEntity<CarsResponse> getOwnedCars() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(carService.getAllOwnedCars(user.getId()));
    }

    @PatchMapping
    public ResponseEntity<Void> setEnableCar(@RequestParam(name = "id") Long id,
                                             @RequestParam(name = "enable") Boolean enable) {
        carService.enableCar(id, enable);
        return ResponseEntity.noContent().build();
    }

}