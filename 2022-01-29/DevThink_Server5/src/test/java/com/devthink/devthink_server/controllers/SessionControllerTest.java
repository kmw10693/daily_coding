package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.application.AuthenticationService;
import com.devthink.devthink_server.errors.LoginFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login("tester@email.com", "test")).willReturn("a.b.c");

        given(authenticationService.login("badbad@email.com", "test")).willThrow(new LoginFailException("badbad@demail.com"));

        given(authenticationService.login("tester@email.com", "xxx")).willThrow(new LoginFailException("badbad@email.com"));
    }


    @Test
    void 올바른_email과_비밀번호로_로그인하는_경우() throws Exception {
        mvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@email.com\",\"password\":\"test\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));

    }

    @Test
    void 잘못된_email로_로그인하는_경우() throws Exception {
        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"badbad@email.com\",\"password\":\"test\"}")
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    void 잘못된_비밀번호로_로그인하는_경우() throws Exception {
        mvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@email.com\", \"password\":\"xxx\"}")
                )
                .andExpect(status().isBadRequest());
    }

}
