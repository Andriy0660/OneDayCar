package com.example.onedaycar.repository;

import com.example.onedaycar.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByIsDisabledFalseAndLocationIs(String location);
    List<Car> findAllByOwnerId(Long id);
}
