package com.example.onedaycar.repository;

import com.example.onedaycar.entity.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    default List<Car> findCarsByCriteriaAndLocationAndIsNotDisabled(String vendor, String carType, Double maxPriceForDay, String location) {
        return findAll((Specification<Car>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (vendor != null) {
                predicates.add(criteriaBuilder.equal(root.get("vendor"), vendor));
            }
            if (carType != null) {
                predicates.add(criteriaBuilder.equal(root.get("carType"), carType));
            }
            if (maxPriceForDay != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("priceForDay"), maxPriceForDay));
            }

            predicates.add(criteriaBuilder.equal(root.get("location"), location));
            predicates.add(criteriaBuilder.isFalse(root.get("isDisabled")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    List<Car> findAllByOwnerId(Long id);
}
