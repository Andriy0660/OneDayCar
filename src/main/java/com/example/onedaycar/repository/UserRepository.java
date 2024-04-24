package com.example.onedaycar.repository;

import com.example.onedaycar.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsUserByEmail(String email);

    Boolean existsUserByPhone(String phone);

    @NotNull List<User> findAllById(@NotNull Iterable<Long> ids);
}
