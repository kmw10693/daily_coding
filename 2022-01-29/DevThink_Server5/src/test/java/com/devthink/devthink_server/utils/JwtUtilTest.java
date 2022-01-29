package com.devthink.devthink_server.utils;

import com.devthink.devthink_server.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class JwtUtilTest {
    private JwtUtil jwtUtil;
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = TOKEN + "WRONG";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }
    
    @Test
    void encode() {
        Long userId = 1L;
        String token = jwtUtil.encode(userId);

        Claims claims = jwtUtil.decode(token);

        assertThat(claims.get("userId", Long.class)).isEqualTo(userId);
    }
                                 
    @Test
    void decode() {
        Claims claims = jwtUtil.decode(TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    void 올바르지_않은_토큰() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 빈토큰으로_decode() {
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(() -> jwtUtil.decode("   "))
                .isInstanceOf(InvalidTokenException.class);
    }

}
