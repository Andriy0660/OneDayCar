package com.example.onedaycar.service;

import com.example.onedaycar.dto.response.CarRequestsResponse;
import com.example.onedaycar.entity.CarRequest;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.repository.CarRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarRequestService {
    private final CarRequestRepository carRequestRepository;

    public void save(CarRequest carRequest) {
        carRequestRepository.save(carRequest);
    }

    public CarRequest findById(Long id) {
        return carRequestRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Car Request with such id does not exist"));
    }

    public List<CarRequest> getAllByStatus(String status) {
        return carRequestRepository.findAllByStatusIs(status);
    }

    public CarRequestsResponse getAllCarRequests(Long userId) {
        List<CarRequest> carRequests = carRequestRepository.findAll();
        List<CarRequestsResponse.CarRequestResponse> carRequestsResponse = carRequests.stream()
                .filter((c) -> !Objects.equals(c.getRenter().getId(), userId) &&
                        c.getStatus() == null).map(c ->
                        CarRequestsResponse.CarRequestResponse.builder()
                                .id(c.getId())
                                .startDate(c.getStartDate())
                                .endDate(c.getEndDate())
                                .carType(c.getCarType())
                                .priceForDay(c.getPriceForDay())
                                .location(c.getLocation())
                                .status(c.getStatus())
                                .renterFirstName(c.getRenter().getFirstName())
                                .renterLastName(c.getRenter().getLastName())
                                .build())
                .toList();
        return new CarRequestsResponse(carRequestsResponse);
    }

    public CarRequestsResponse getPersonalCarRequests(Long id, Boolean byOwner) {
        if (byOwner) {
            return getPersonalCarRequestsByOwner(id);
        } else {
            return getPersonalCarRequestsByRenter(id);
        }
    }

    private CarRequestsResponse getPersonalCarRequestsByRenter(Long id) {
        List<CarRequest> carRequests;
        carRequests = carRequestRepository.findAllByRenterId(id);
        return new CarRequestsResponse(carRequests
                .stream()
                .map(b -> CarRequestsResponse.CarRequestResponse.builder()
                        .id(b.getId())
                        .ownerFirstName(b.getOwner().getFirstName())
                        .ownerLastName(b.getOwner().getLastName())
                        .startDate(b.getStartDate())
                        .endDate(b.getEndDate())
                        .carType(b.getCarType())
                        .location(b.getLocation())
                        .priceForDay(b.getPriceForDay())
                        .minYear(b.getMin_year())
                        .status(b.getStatus())
                        .build())
                .toList());
    }

    private CarRequestsResponse getPersonalCarRequestsByOwner(Long id) {
        List<CarRequest> carRequests;
        carRequests = carRequestRepository.findAllByOwnerId(id);

        return new CarRequestsResponse(carRequests
                .stream()
                .map(b -> CarRequestsResponse.CarRequestResponse.builder()
                        .id(b.getId())
                        .renterFirstName(b.getRenter().getFirstName())
                        .renterLastName(b.getRenter().getLastName())
                        .startDate(b.getStartDate())
                        .endDate(b.getEndDate())
                        .carType(b.getCarType())
                        .location(b.getLocation())
                        .priceForDay(b.getPriceForDay())
                        .minYear(b.getMin_year())
                        .status(b.getStatus())
                        .build())
                .toList());
    }
}
