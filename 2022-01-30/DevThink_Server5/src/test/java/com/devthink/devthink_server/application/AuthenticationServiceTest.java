package com.devthink.devthink_server.application;

import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.infra.UserRepository;
import com.devthink.devthink_server.errors.LoginFailException;
import com.devthink.devthink_server.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {
    private static final String secret = "12345678901234567890123456789012";
    private AuthenticationService authenticationService;
    private String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {

        JwtUtil jwtUtil = new JwtUtil(secret);
        authenticationService = new AuthenticationService(userRepository, jwtUtil);
        User user = User.builder().password("test1234567").build();
        given(userRepository.findByEmail("tester@email.com")).willReturn(Optional.of(user));
    }
    
    @Test
    void 올바른_이메일과_비밀번호로_로그인() {
        String accessToken = authenticationService.login("tester@email.com", "test1234567");

        assertThat(accessToken).isEqualTo(VALID_TOKEN);

        verify(userRepository).findByEmail("tester@email.com");
    }

    @Test
    void 올바르지_않은_이메일로_로그인() {
        assertThatThrownBy(
                () -> authenticationService.login("badguy@email.com", "test")
        ).isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("badguy@email.com");

    }

    @Test
    void 올바르지_않은_비밀번호로_로그인() {
        assertThatThrownBy(
                () -> authenticationService.login("tester@email.com", "xxx")
        ).isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("tester@email.com");
    }
}
