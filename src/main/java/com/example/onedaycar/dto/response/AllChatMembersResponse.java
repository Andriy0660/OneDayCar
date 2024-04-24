package com.example.onedaycar.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllChatMembersResponse {
    private List<ChatMemberResponse> chatMembers;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMemberResponse {
        private Long id;
        private String firstName;
        private String lastName;
    }
}
