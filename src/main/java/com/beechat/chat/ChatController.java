package com.beechat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage") // Handles incoming messages
    @SendTo("/topic/public") // sent to this endpoint
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }


    @MessageMapping("/chat.addUser") // Client sends a request
    @SendTo("topic/public") // Announces a user has joined
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Stores username in websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage; // Message sent & visibile to the group
    }

}
