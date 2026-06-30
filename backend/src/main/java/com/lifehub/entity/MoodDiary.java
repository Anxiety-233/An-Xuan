package com.lifehub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("mood_diaries")
public class MoodDiary {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String moodEmoji;
    private Integer moodLevel;
    private String content;
    private Boolean isVisibleToPartner;
    private LocalDate diaryDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
