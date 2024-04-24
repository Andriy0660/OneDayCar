package com.example.onedaycar.controller;


import com.example.onedaycar.dto.request.SendMessageRequest;
import com.example.onedaycar.dto.response.AllChatMembersResponse;
import com.example.onedaycar.dto.response.MessageResponse;
import com.example.onedaycar.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    public ResponseEntity<Void> addMessage(@RequestBody SendMessageRequest sendMessageRequest) {
        messageService.addMessage(sendMessageRequest);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages(@RequestParam(name = "senderId") Long senderId,
                                                             @RequestParam(name = "receiverId") Long receiverId) {
        return ResponseEntity.ok(messageService.getMessages(senderId, receiverId));
    }
    @GetMapping("/allChats")
    public ResponseEntity<AllChatMembersResponse> getAllChatMembers(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok(messageService.getAllChatMembers(id));
    }
}
