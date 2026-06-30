# Life Hub - 三合一综合网站

集成个人成长记录、情侣互动、角色卡片系统的全栈 Web 应用。

## 功能模块

### 1. 个人成长记录
- 每日学习记录
- 标签分类
- 学习时长统计
- 时间轴展示
- 数据可视化（学习趋势图）

### 2. 情侣互动空间
- **相册墙**：上传合照，实时同步
- **心情日记**：每日心情打卡（emoji + 文字）
- **留言板**：私密留言和公开留言
- **纪念日倒计时**：重要日期提醒
- **小游戏**：猜拳、简单互动游戏（可开挂让你赢😄）

### 3. 角色卡片系统
- **朋友恶搞**：将朋友 P 成斗罗大陆角色
- **角色详情页**：技能、属性、背景故事
- **互动功能**：点赞、评论、分享
- **数据统计**：浏览量、人气排行

## 技术栈

### 前端
- Vue 3 + Vite
- TailwindCSS（暖色调设计）
- Axios
- Vue Router
- Pinia（状态管理）

### 后端
- Spring Boot 3
- MyBatis-Plus
- MySQL 8.0
- JWT 认证
- Spring Security

### 部署
- Docker + Docker Compose
- Nginx（前端静态资源）

## 快速启动

### 方式一：Docker 一键启动（推荐）

```bash
# 克隆项目后，直接启动
docker compose up -d

# 访问
# 前端: http://localhost
# 后端 API: http://localhost:8080
```

### 方式二：本地开发

#### 后端
```bash
cd backend
mvn spring-boot:run
```

#### 前端
```bash
cd frontend
npm install
npm run dev
```

#### 数据库
```bash
# 导入数据库
mysql -u root -p < schema.sql
```

## 默认账号

- 管理员：`admin` / `admin123`
- 用户1：`user1` / `admin123`（情侣账号A）
- 用户2：`user2` / `admin123`（情侣账号B）

## 项目结构

```
life-hub/
├── frontend/              # Vue 3 前端
│   ├── src/
│   │   ├── views/        # 页面组件
│   │   ├── components/   # 公共组件
│   │   ├── api/          # API 接口
│   │   ├── store/        # 状态管理
│   │   └── router/       # 路由配置
│   ├── Dockerfile
│   └── package.json
│
├── backend/              # Spring Boot 后端
│   ├── src/main/java/com/lifehub/
│   │   ├── entity/       # 实体类
│   │   ├── mapper/       # MyBatis Mapper
│   │   ├── service/      # 业务逻辑
│   │   ├── controller/   # 控制器
│   │   └── config/       # 配置类
│   ├── Dockerfile
│   └── pom.xml
│
├── schema.sql            # 数据库建表脚本
├── docker-compose.yml    # Docker 编排配置
└── README.md
```

## 核心 API 接口

### 用户认证
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/auth/me` - 获取当前用户信息

### 成长记录
- `GET /api/growth` - 获取记录列表
- `POST /api/growth` - 创建记录
- `PUT /api/growth/{id}` - 更新记录
- `DELETE /api/growth/{id}` - 删除记录

### 相册
- `GET /api/albums` - 获取相册列表
- `POST /api/albums` - 创建相册
- `POST /api/photos` - 上传照片
- `GET /api/albums/{id}/photos` - 获取相册照片

### 心情日记
- `GET /api/moods` - 获取心情记录
- `POST /api/moods` - 创建心情记录
- `GET /api/moods/partner` - 查看伴侣心情

### 角色卡片
- `GET /api/characters` - 获取角色列表
- `GET /api/characters/{id}` - 获取角色详情
- `POST /api/characters` - 创建角色
- `POST /api/characters/{id}/like` - 点赞

### 留言板
- `GET /api/messages` - 获取留言列表
- `POST /api/messages` - 发送留言

## 开发计划

- [ ] 基础框架搭建
- [ ] 用户认证系统
- [ ] 成长记录 CRUD
- [ ] 相册上传功能
- [ ] 心情日记模块
- [ ] 角色卡片系统
- [ ] 小游戏实现
- [ ] 实时消息推送（WebSocket）
- [ ] 移动端适配
- [ ] 数据导出功能

## 服务器配置建议

### 最低配置
- 2核 2G 内存
- 40G 硬盘
- 适合日访问量 < 500

### 推荐配置
- 4核 4G 内存
- 100G 硬盘
- 适合日访问量 < 5000

## License

MIT
