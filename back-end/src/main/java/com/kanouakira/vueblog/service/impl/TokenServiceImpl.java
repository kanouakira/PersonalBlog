package com.kanouakira.vueblog.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kanouakira.vueblog.entity.User;
import com.kanouakira.vueblog.service.TokenService;
import org.springframework.stereotype.Service;

@Service("TokenService")
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(User user) {
        String token = "";
        token = JWT.create().withAudience(String.valueOf(user.getId()))
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
