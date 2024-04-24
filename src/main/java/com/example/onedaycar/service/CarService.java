package com.example.onedaycar.service;

import com.example.onedaycar.dto.request.GetCarsRequest;
import com.example.onedaycar.dto.response.CarsResponse;
import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.entity.Car;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.repository.CarRepository;
import com.example.onedaycar.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
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

    public CarsResponse getAllAvailableCars(Long userId, GetCarsRequest r) {

        List<Car> availableCars = carRepository.
                findCarsByCriteriaAndLocationAndIsNotDisabled(
                        r.getVendor(),
                        r.getCarType(),
                        r.getMaxPrice(),
                        r.getLocation())
                .stream()
                .filter(c -> !Objects.equals(c.getOwner().getId(), userId) &&
                        isCarAvailable(c, r.getStartDate(), r.getEndDate()))
                .toList();

        int totalPages = getTotalPages(availableCars, r.getPageSize());
        List<Car> pagedCars = getPage(availableCars, r.getPage(), r.getPageSize());

        return CarsResponse.builder().userId(userId).countOfPages(totalPages).cars(pagedCars.stream()
                .map(c -> CarsResponse.CarResponse.builder()
                        .id(c.getId())
                        .ownerId(c.getOwnerId())
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
                .toList()).build();
    }

    public List<Car> getPage(List<Car> list, int page, int pageSize) {
        int fromIndex = page * pageSize;
        if (fromIndex >= list.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + pageSize, list.size());
        return list.subList(fromIndex, toIndex);
    }

    public int getTotalPages(List<Car> list, int pageSize) {
        int totalElements = list.size();
        int totalPages = totalElements / pageSize;
        if (totalElements % pageSize != 0) {
            totalPages++;
        }
        return totalPages;
    }


    public CarsResponse getAllOwnedCars(Long id) {
        List<Car> ownedCars = carRepository.findAllByOwnerId(id);

        return CarsResponse.builder().cars(ownedCars.stream()
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
                .toList()).build();
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
