package com.example.onedaycar.service;

import com.example.onedaycar.dto.response.CarsResponse;
import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.entity.Car;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.repository.CarRepository;
import com.example.onedaycar.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    //TODO edit car
    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Car with such id does not exist"));
    }

    public void save(Car car) {
        carRepository.save(car);
    }

    public CarsResponse getAllAvailableCars(Long userId,
                                                 LocalDate startDate, LocalDate endDate,
                                                 String location) {

        List<Car> availableCars = carRepository.findAllByIsDisabledFalseAndLocationIs(location).stream()
                .filter(c -> !Objects.equals(c.getOwner().getId(), userId) &&
                        isCarAvailable(c, startDate, endDate))
                .toList();

        return new CarsResponse(availableCars.stream()
                .map(c -> CarsResponse.CarResponse.builder()
                        .id(c.getId())
                        .ownerFirstName(c.getOwner().getFirstName())
                        .ownerLastName(c.getOwner().getLastName())
                        .ownerPhone(c.getOwner().getPhone())
                        .vendor(c.getVendor())
                        .model(c.getModel())
                        .carType(c.getCarType())
                        .location(c.getLocation())
                        .priceForDay(c.getPriceForDay())
                        .description(c.getDescription())
                        .year(c.getYear())
                        .build())
                .toList());
    }

    public CarsResponse getAllOwnedCars(Long id) {
        List<Car> ownedCars = carRepository.findAllByOwnerId(id);

        return new CarsResponse(ownedCars.stream()
                .map(c -> CarsResponse.CarResponse.builder()
                        .id(c.getId())
                        .vendor(c.getVendor())
                        .model(c.getModel())
                        .carType(c.getCarType())
                        .location(c.getLocation())
                        .priceForDay(c.getPriceForDay())
                        .description(c.getDescription())
                        .year(c.getYear())
                        .isDisabled(c.getIsDisabled())
                        .build())
                .toList());
    }

    public void enableCar(Long id, Boolean enable) {
        Car car = carRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Car with such id does not exist"));
        car.setIsDisabled(!enable);
        carRepository.save(car);
    }

    //TODO додати можливість додавати конфліктні букінги щоб власник вибрав сам який підтвердити
    public boolean isCarAvailable(Car car, LocalDate startDate, LocalDate endDate) {
        for (Booking booking : car.getBookings()) {
            if (booking.getStatus().equals(Status.REJECTED.toString()) ||
                    booking.getStatus().equals(Status.DONE.toString()))
                continue;
            if (!endDate.isBefore(booking.getStartDate()) && !startDate.isAfter(booking.getEndDate()))
                return false;
        }
        return true;
    }
}
