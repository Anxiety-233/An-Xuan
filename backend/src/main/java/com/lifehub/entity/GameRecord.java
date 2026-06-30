package com.lifehub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("game_records")
public class GameRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String gameType;
    private Long player1Id;
    private Long player2Id;
    private Long winnerId;
    private String gameData; // JSON string
    private Integer duration;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
