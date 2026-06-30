package com.lifehub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("photos")
public class Photo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long albumId;
    private String imageUrl;
    private String thumbnailUrl;
    private String description;
    private LocalDate takenDate;
    private Long uploadedBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
