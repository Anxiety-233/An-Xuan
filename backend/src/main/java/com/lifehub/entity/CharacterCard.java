package com.lifehub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("character_cards")
public class CharacterCard {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String originalPerson;
    private String avatar;
    private String title;
    private Integer level;
    private Integer starRating;
    private Integer attack;
    private Integer defense;
    private Integer speed;
    private String skillName;
    private String skillDescription;
    private String backgroundStory;
    private String personalityTags;
    private String famousQuote;
    private String series;
    private Boolean isPublic;
    private Long createdBy;
    private Integer viewCount;
    private Integer likeCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
