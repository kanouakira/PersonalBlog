package com.kanouakira.vueblog.service;

import com.kanouakira.vueblog.entity.User;

public interface TokenService {
    /**
     * 生成token
     */
    public String getToken(User user);
}
