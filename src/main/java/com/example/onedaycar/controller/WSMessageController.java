package com.example.onedaycar.controller;

import com.example.onedaycar.dto.request.SendMessageRequest;
import com.example.onedaycar.dto.response.MessageResponse;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class WSMessageController {
    private final UserService userService;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public MessageResponse publishMessage(@DestinationVariable String chatId,
                                          @Payload SendMessageRequest messageRequest) {
        User user = userService.findById(messageRequest.getSenderId());
        return MessageResponse.builder()
                .senderId(messageRequest.getSenderId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .content(messageRequest.getContent())
                .time(LocalDateTime.now())
                .build();

    }
}