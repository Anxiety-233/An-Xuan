package com.lifehub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("character_interactions")
public class CharacterInteraction {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long characterId;
    private Long userId;
    private String interactionType; // view, like, comment, share
    private String commentContent;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
