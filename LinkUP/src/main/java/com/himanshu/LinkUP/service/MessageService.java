package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.MessageResponse;
import com.himanshu.LinkUP.dto.SendMessageRequest;
import com.himanshu.LinkUP.entity.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(SendMessageRequest request);

    List<MessageResponse> getConversation(Long userId);

    // for websocket
    Message sendWebSocketMessage(
            String email,
            SendMessageRequest request
    );
}
