package com.example.onedaycar.service;

import com.example.onedaycar.dto.request.SendMessageRequest;
import com.example.onedaycar.dto.response.AllChatMembersResponse;
import com.example.onedaycar.dto.response.MessageResponse;
import com.example.onedaycar.entity.Message;
import com.example.onedaycar.entity.User;
import com.example.onedaycar.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;

    public void addMessage(SendMessageRequest sendMessageRequest) {
        messageRepository.save(Message.builder()
                .content(sendMessageRequest.getContent())
                .senderId(sendMessageRequest.getSenderId())
                .receiverId(sendMessageRequest.getReceiverId())
                .time(LocalDateTime.now())
                .build());
    }

    public List<MessageResponse> getMessages(Long senderId, Long receiverId) {

        List<MessageResponse> sent = messageRepository.findAllBySenderIdIsAndReceiverIdIs(senderId, receiverId).stream()
                .map((msg) -> {
                            User user = userService.findById(senderId);
                            return MessageResponse.builder()
                                    .senderId(user.getId())
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .content(msg.getContent())
                                    .time(msg.getTime())
                                    .build();
                        }
                ).toList();

        List<MessageResponse> received = messageRepository.findAllBySenderIdIsAndReceiverIdIs(receiverId, senderId).stream()
                .map((msg) -> {
                            User user = userService.findById(receiverId);
                            return MessageResponse.builder()
                                    .senderId(user.getId())
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .content(msg.getContent())
                                    .time(msg.getTime())
                                    .build();
                        }
                ).toList();

        List<MessageResponse> res = new ArrayList<>(sent);
        res.addAll(received);

        return res.stream().sorted(Comparator.comparing(MessageResponse::getTime)).toList();
    }

    public AllChatMembersResponse getAllChatMembers(Long userId) {
        List<Message> messages = messageRepository.findBySenderIdOrReceiverId(userId, userId);

        Set<Long> userIds = new HashSet<>();

        for (Message message : messages) {
            if (!Objects.equals(message.getSenderId(), userId)) {
                userIds.add(message.getSenderId());
            }
            if (!Objects.equals(message.getReceiverId(), userId)) {
                userIds.add(message.getReceiverId());
            }
        }

        List<User> users = userService.findAllByIds(userIds);
        return new AllChatMembersResponse(users.stream()
                .map(user -> AllChatMembersResponse.ChatMemberResponse
                        .builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build()).toList()
        );

    }
}
