-- 三合一综合网站数据库设计
-- 包含：个人成长记录 + 情侣互动 + 角色卡片系统

CREATE DATABASE IF NOT EXISTS life_hub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE life_hub;

-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    email VARCHAR(100) COMMENT '邮箱',
    role ENUM('user', 'couple_a', 'couple_b', 'admin') DEFAULT 'user' COMMENT '角色类型',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 成长记录表
CREATE TABLE growth_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT COMMENT '学习内容',
    tags VARCHAR(500) COMMENT '标签（逗号分隔）',
    study_duration INT COMMENT '学习时长（分钟）',
    record_date DATE NOT NULL COMMENT '记录日期',
    is_public TINYINT(1) DEFAULT 0 COMMENT '是否公开',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_date (user_id, record_date),
    INDEX idx_tags (tags)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人成长记录表';

-- 相册表
CREATE TABLE albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL COMMENT '相册标题',
    description TEXT COMMENT '描述',
    cover_image VARCHAR(255) COMMENT '封面图',
    is_couple_shared TINYINT(1) DEFAULT 1 COMMENT '是否情侣共享',
    created_by BIGINT NOT NULL COMMENT '创建者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='相册表';

-- 照片表
CREATE TABLE photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    album_id BIGINT NOT NULL COMMENT '所属相册ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
    thumbnail_url VARCHAR(255) COMMENT '缩略图URL',
    description TEXT COMMENT '照片描述',
    taken_date DATE COMMENT '拍摄日期',
    uploaded_by BIGINT NOT NULL COMMENT '上传者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_album (album_id),
    INDEX idx_date (taken_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='照片表';

-- 心情日记表
CREATE TABLE mood_diaries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    mood_emoji VARCHAR(10) NOT NULL COMMENT '心情表情',
    mood_level TINYINT COMMENT '心情等级 1-5',
    content TEXT COMMENT '日记内容',
    is_visible_to_partner TINYINT(1) DEFAULT 1 COMMENT '对伴侣可见',
    diary_date DATE NOT NULL COMMENT '日记日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_date (user_id, diary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心情日记表';

-- 留言板表
CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    from_user_id BIGINT NOT NULL COMMENT '发送者ID',
    to_user_id BIGINT COMMENT '接收者ID（NULL表示公开留言）',
    content TEXT NOT NULL COMMENT '留言内容',
    is_read TINYINT(1) DEFAULT 0 COMMENT '是否已读',
    parent_id BIGINT COMMENT '父留言ID（回复功能）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES messages(id) ON DELETE CASCADE,
    INDEX idx_from_user (from_user_id),
    INDEX idx_to_user (to_user_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言板表';

-- 纪念日表
CREATE TABLE anniversaries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL COMMENT '纪念日名称',
    anniversary_date DATE NOT NULL COMMENT '纪念日日期',
    description TEXT COMMENT '描述',
    is_recurring TINYINT(1) DEFAULT 1 COMMENT '是否每年重复',
    icon VARCHAR(50) COMMENT '图标',
    created_by BIGINT NOT NULL COMMENT '创建者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_date (anniversary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纪念日表';

-- 角色卡片表（恶搞朋友系统）
CREATE TABLE character_cards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    original_person VARCHAR(50) COMMENT '原型人物',
    avatar VARCHAR(255) NOT NULL COMMENT '角色头像',
    title VARCHAR(100) COMMENT '称号',
    level INT DEFAULT 1 COMMENT '等级',
    star_rating TINYINT DEFAULT 3 COMMENT '星级 1-5',
    attack INT COMMENT '攻击力',
    defense INT COMMENT '防御力',
    speed INT COMMENT '速度',
    skill_name VARCHAR(100) COMMENT '技能名称',
    skill_description TEXT COMMENT '技能描述',
    background_story TEXT COMMENT '背景故事',
    personality_tags VARCHAR(255) COMMENT '性格标签',
    famous_quote TEXT COMMENT '名言',
    series VARCHAR(50) DEFAULT '斗罗大陆' COMMENT '所属系列',
    is_public TINYINT(1) DEFAULT 1 COMMENT '是否公开',
    created_by BIGINT NOT NULL COMMENT '创建者ID',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_name (name),
    INDEX idx_series (series),
    INDEX idx_like (like_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色卡片表';

-- 角色互动记录表
CREATE TABLE character_interactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    character_id BIGINT NOT NULL COMMENT '角色ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    interaction_type ENUM('view', 'like', 'comment', 'share') COMMENT '互动类型',
    comment_content TEXT COMMENT '评论内容',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (character_id) REFERENCES character_cards(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_character (character_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色互动记录表';

-- 小游戏记录表
CREATE TABLE game_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_type VARCHAR(50) NOT NULL COMMENT '游戏类型',
    player1_id BIGINT NOT NULL COMMENT '玩家1 ID',
    player2_id BIGINT COMMENT '玩家2 ID',
    winner_id BIGINT COMMENT '获胜者ID',
    game_data JSON COMMENT '游戏数据（JSON格式）',
    duration INT COMMENT '游戏时长（秒）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player1_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (player2_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_game_type (game_type),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小游戏记录表';

-- 初始化管理员账号（密码: admin123 的 BCrypt 加密）
INSERT INTO users (username, password, nickname, role) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin');

-- 插入示例数据（可选）
INSERT INTO users (username, password, nickname, role) VALUES
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '小明', 'couple_a'),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '小红', 'couple_b');
