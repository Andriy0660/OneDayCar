package com.example.onedaycar.controller;

import com.example.onedaycar.dto.request.AddCarReqRequest;
import com.example.onedaycar.dto.response.CarRequestsResponse;
import com.example.onedaycar.entity.CarRequest;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.exception.ConflictException;
import com.example.onedaycar.service.CarRequestService;
import com.example.onedaycar.util.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carRequest")
@RequiredArgsConstructor
public class CarRequestController {
    private final CarRequestService carRequestService;

    @PostMapping
    public ResponseEntity<Void> addCarRequest(@RequestBody @Valid AddCarReqRequest request) {
        User renter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        carRequestService.save(CarRequest.builder()
                .carType(request.getCarType())
                .priceForDay(request.getPriceForDay())
                .location(request.getLocation())
                .renter(renter)
                .min_year(request.getMinYear())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping
    public ResponseEntity<CarRequestsResponse> getAllCarRequests() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(carRequestService.getAllCarRequests(user.getId()));
    }

    @PatchMapping("/confirm")
    public ResponseEntity<Void> confirmCarRequest(@RequestParam Long carRequestId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CarRequest carRequest = carRequestService.findById(carRequestId);

        if (carRequest.getStatus() != null)
            throw new ConflictException("Somebody accepted car request(");

        carRequest.setStatus(Status.CREATED.toString());
        carRequest.setOwner(user);
        carRequestService.save(carRequest);
        return ResponseEntity.noContent().build();
    }

    // якщо користувач вручну відхиляє ставимо null якщо приймає DONE
    @PatchMapping("/setStatus")
    public ResponseEntity<Void> setStatus(@RequestParam(name = "carRequestId") Long carRequestId,
                                          @RequestParam(name = "status") Status status) {

        CarRequest carRequest = carRequestService.findById(carRequestId);

        if (status == null) {
            carRequest.setOwner(null);
            carRequest.setStatus(null);
        } else {
            carRequest.setStatus(status.toString());
        }
        carRequestService.save(carRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/personal")
    public ResponseEntity<CarRequestsResponse> getPersonalCarRequests(@RequestParam Boolean byOwner) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(carRequestService.getPersonalCarRequests(user.getId(), byOwner));
    }

}
