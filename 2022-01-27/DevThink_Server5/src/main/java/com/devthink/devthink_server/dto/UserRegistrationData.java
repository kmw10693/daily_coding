package com.devthink.devthink_server.dto;

import com.devthink.devthink_server.domain.BaseTimeEntity;
import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@AllArgsConstructor
public class UserRegistrationData extends BaseTimeEntity {
    @NotBlank(message = "이메일 주소를 입력하세요.")
    @Email
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    @Size(min=8)
    private String password;

    @NotBlank
    @Mapping("phoneNum")
    @Size(max=13)
    private String phoneNum;

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max=8)
    @Mapping("nickname")
    private String nickname;

    @NotBlank(message = "직무를 선택해주세요.")
    @Mapping("role")
    private String role;

    @Mapping("stack")
    private List<String> stack;
    
    @Mapping("blogAddr")
    private String blogAddr;

    @Mapping("gitNickname")
    private String gitNickname;

    @Mapping("point")
    private Integer point;

    @Mapping("deleted")
    private boolean deleted;

    public UserRegistrationData() {
        this.stack = new ArrayList<>();
        this.blogAddr = "";
        this.gitNickname = "";
        this.point = 0;
    }
}
