package com.lifehub.controller;

import com.lifehub.common.Result;
import com.lifehub.entity.Message;
import com.lifehub.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Resource
    private MessageService messageService;

    @GetMapping
    public Result<List<Message>> getMessages(
            @RequestParam Long user1Id,
            @RequestParam(required = false) Long user2Id) {
        if (user2Id == null) {
            // 获取所有与user1相关的消息
            List<Message> messages = messageService.list();
            return Result.success(messages);
        }
        List<Message> messages = messageService.getMessagesBetweenUsers(user1Id, user2Id);
        return Result.success(messages);
    }

    @PostMapping
    public Result<Message> sendMessage(@RequestBody Message message) {
        Message sent = messageService.sendMessage(message);
        return Result.success(sent);
    }

    @PutMapping("/{id}/read")
    public Result<String> markAsRead(
            @PathVariable Long id,
            @RequestParam Long userId) {
        boolean success = messageService.markAsRead(id, userId);
        return success ? Result.success("已标记为已读") : Result.error("操作失败");
    }
}
