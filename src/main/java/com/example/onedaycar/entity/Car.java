package com.example.onedaycar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(insertable=false, updatable=false, name = "owner_id")
    private Long ownerId;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "model")
    private String model;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "year")
    private Integer year;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "price_for_day")
    private Double priceForDay;

    @Column(name = "is_disabled")
    private Boolean isDisabled;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    Set<Booking> bookings = new HashSet<>();

}
