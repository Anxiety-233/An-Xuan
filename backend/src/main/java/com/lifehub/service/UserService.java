package com.lifehub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.User;

public interface UserService extends IService<User> {
    User getUserByUsername(String username);
    User register(String username, String password, String nickname);
    boolean existsByUsername(String username);
}
