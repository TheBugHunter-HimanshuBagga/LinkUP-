package com.himanshu.LinkUP.service;

import com.himanshu.LinkUP.dto.MessageResponse;
import com.himanshu.LinkUP.dto.SendMessageRequest;

import java.util.List;

public interface MessageService {

    String sendMessage(SendMessageRequest request);

    List<MessageResponse> getConversation(Long userId);
}
