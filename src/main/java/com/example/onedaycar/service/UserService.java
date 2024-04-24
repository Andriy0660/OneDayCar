package com.example.onedaycar.service;

import com.example.onedaycar.dto.request.UpdateProfileRequest;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.exception.BadRequestException;
import com.example.onedaycar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    }

    public Boolean existsUserByPhone(String phone) {
        return userRepository.existsUserByPhone(phone);
    }

    public Boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateUser(UpdateProfileRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        userRepository.save(user);
    }

    public List<User> findAllByIds(Set<Long> ids) {
        return userRepository.findAllById(ids);
    }
}
