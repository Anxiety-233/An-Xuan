package com.lifehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifehub.entity.Message;
import com.lifehub.mapper.MessageMapper;
import com.lifehub.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> getMessagesBetweenUsers(Long user1Id, Long user2Id) {
        return this.list(new LambdaQueryWrapper<Message>()
                .and(wrapper -> wrapper
                        .eq(Message::getFromUserId, user1Id)
                        .eq(Message::getToUserId, user2Id))
                .or(wrapper -> wrapper
                        .eq(Message::getFromUserId, user2Id)
                        .eq(Message::getToUserId, user1Id))
                .orderByAsc(Message::getCreatedAt));
    }

    @Override
    public Message sendMessage(Message message) {
        if (message.getIsRead() == null) {
            message.setIsRead(false);
        }
        this.save(message);
        return message;
    }

    @Override
    public boolean markAsRead(Long messageId, Long userId) {
        Message message = this.getById(messageId);
        if (message != null && message.getToUserId().equals(userId)) {
            message.setIsRead(true);
            return this.updateById(message);
        }
        return false;
    }
}
