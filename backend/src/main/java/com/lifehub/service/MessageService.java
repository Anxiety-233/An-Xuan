package com.lifehub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<Message> getMessagesBetweenUsers(Long user1Id, Long user2Id);
    Message sendMessage(Message message);
    boolean markAsRead(Long messageId, Long userId);
}
