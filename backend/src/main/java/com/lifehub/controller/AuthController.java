package com.lifehub.controller;

import com.lifehub.common.Result;
import com.lifehub.dto.LoginRequest;
import com.lifehub.dto.RegisterRequest;
import com.lifehub.entity.User;
import com.lifehub.service.UserService;
import com.lifehub.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getNickname()
            );
            // 清除密码信息
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = userService.getUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(user.getUsername());

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);

            return Result.success(result);
        } catch (AuthenticationException e) {
            return Result.error(401, "用户名或密码错误");
        }
    }

    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwt);
            User user = userService.getUserByUsername(username);
            if (user != null) {
                user.setPassword(null);
                return Result.success(user);
            }
            return Result.error(404, "用户不存在");
        } catch (Exception e) {
            return Result.error(401, "无效的令牌");
        }
    }
}
