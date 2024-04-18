package com.example.onedaycar.repository;

import com.example.onedaycar.entity.Booking;
import com.example.onedaycar.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByRenterId(Long id);
    List<Booking> findAllByStatusIs(String status);
}
