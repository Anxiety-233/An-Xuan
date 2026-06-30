package com.lifehub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("anniversaries")
public class Anniversary {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private LocalDate anniversaryDate;
    private String description;
    private Boolean isRecurring;
    private String icon;
    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
