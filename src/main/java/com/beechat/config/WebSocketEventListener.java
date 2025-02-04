package com.beechat.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.beechat.chat.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

 // Used to send messages to STOMP topics (like broadcasting to a group chat) - injected automatically with @RequiredArgsConstructor
 private final SimpMessageSendingOperations messageTemplate;


 @EventListener // Listens for websocket disconnect events
 public void handleWebSocketDisconnectEvent (SessionDisconnectEvent event) {

  // Get headers and username
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String username = (String) headerAccessor.getSessionAttributes().get("username");

    if(username != null) {
        log.info("User disconnected: {}", username);
        let chatMessage = ChatMessage.builder() // Lombok builder pattern
        .type(MessageType.LEAVE)
        .sender(username)
        .build();
        messageTemplate.convertAndSend("/topic/public", chatMessage);
    }
 }
}
