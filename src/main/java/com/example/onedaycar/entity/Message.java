package com.example.onedaycar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "sender_id")
//    private User sender;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "receiver_id")
//    private User receiver;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private LocalDateTime time;
}
