package com.devthink.devthink_server.application;


import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.infra.UserRepository;
import com.devthink.devthink_server.errors.InvalidTokenException;
import com.devthink.devthink_server.errors.LoginFailException;
import com.devthink.devthink_server.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException(email));

        if(!user.authenticate(password)) {
            throw new LoginFailException(email);
       }
        return jwtUtil.encode(1L);
    }

    public Long parseToken(String token) {
        if(token == null) {
            throw new InvalidTokenException(token);
        }
        Claims claims = jwtUtil.decode(token);
        return claims.get("userId", Long.class);
    }

}
