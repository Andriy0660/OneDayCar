package com.example.onedaycar.schedule;

import com.example.onedaycar.entity.CarRequest;
import com.example.onedaycar.service.CarRequestService;
import com.example.onedaycar.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarRequestSchedule {
    private final CarRequestService carRequestService;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void editCarRequestStatus() {
        List<CarRequest> allCREATEDRequests = carRequestService.getAllByStatus(Status.CREATED.toString());
        LocalDate now = LocalDate.now();
        for (CarRequest carRequest : allCREATEDRequests) {
            if (now.isAfter(carRequest.getStartDate())) {
                carRequest.setStatus(Status.REJECTED.toString());
                carRequestService.save(carRequest);

            }
        }
    }
}
