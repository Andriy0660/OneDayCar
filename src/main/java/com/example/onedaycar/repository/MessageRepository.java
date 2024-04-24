package com.example.onedaycar.repository;

import com.example.onedaycar.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySenderIdIsAndReceiverIdIs(Long senderId, Long receiverId);
    List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
