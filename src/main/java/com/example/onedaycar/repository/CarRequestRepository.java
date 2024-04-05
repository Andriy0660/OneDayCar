package com.example.onedaycar.repository;

import com.example.onedaycar.entity.CarRequest;
import com.example.onedaycar.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRequestRepository extends JpaRepository<CarRequest, Long> {
    List<CarRequest> findAllByStatusIs(String status);

    List<CarRequest> findAllByOwnerId(Long id);
    List<CarRequest> findAllByRenterId(Long id);
}
