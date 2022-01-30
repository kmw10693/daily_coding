// 1. 가입 -> POST /users
// 2. 사용자 정보 갱신 -> PUT/PATCH /users/{id}
// ----> 자기 정보만 갱신 가능.
// 3. 탈퇴 -> DELETE /users/{id}
package com.devthink.devthink_server.controllers;


import com.devthink.devthink_server.application.UserService;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.dto.UserModificationData;
import com.devthink.devthink_server.dto.UserResultData;
import com.devthink.devthink_server.dto.UserRegistrationData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 사용자의 HTTP 요청을 처리하는 클래스입니다.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자의 이메일을 받아와 이미 저장되어 있는 이메일인지 확인합니다.
     * @param userEmail 입력한 사용자의 이메일.
     * @return 이메일이 저장되어 있는지 여부.
     */
    @GetMapping("/emailCheck/{userEmail}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(userService.isDuplicateEmail(userEmail));
    }

    /**
     * 사용자의 닉네임을 받아와 이미 저장되어 있는 닉네임인지 확인합니다.
     * @param nickname 입력한 사용자의 닉네임.
     * @return 닉네임이 저장되어 있는지 여부.
     */
    @GetMapping("/nicknameCheck/{nickname}")
    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.isDuplicateNickname(nickname));
    }

    /**
     * 사용자의 식별자를 전달받아 사용자를 리턴합니다.
     * @param id 사용자의 식별자
     * @return 사용자
     */
    @GetMapping("/{id}")
    public UserResultData detail(@PathVariable Long id) {
        User user = userService.getUser(id);
        return getUserResultData(user);
    }
    
    /**
     * 입력한 valid한 사용자의 정보를 받아 사용자를 생성합니다.
     * @param userRegistrationData 입력한 valid한 사용자의 정보
     * @return 사용자 생성.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResultData create(@RequestBody @Valid UserRegistrationData userRegistrationData) {
        User user = userService.registerUser(userRegistrationData);
        return getUserResultData(user);

    }

    /**
     * 입력한 사용자의 식별자 값과, valid한 사용자의 정보를 받아, 기존의 정보를 입력한 정보로 변경합니다.
     * @param id 사용자의 식별자
     * @param modificationData 사용자의 새로운 정보
     * @return 기존 사용자의 정보 수정
     */
    @PatchMapping("/{id}")
    public UserResultData update(
                    @PathVariable Long id,
                    @RequestBody @Valid UserModificationData modificationData
    ) {
        User user = userService.updateUser(id, modificationData);
        return getUserResultData(user);
    }

    /**
     * 입력한 사용자의 식별자 값을 전달받아, 해당하는 사용자를 삭제합니다.
     * @param id 삭제하고자 하는 사용자의 식별자
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    
    /**
     * 사용자의 정보를 받아, 사용자를 dto 데이터로 변환하여 반환합니다.
     * @param user 사용자 정보
     * @return 입력된 dto 데이터로 변환된 값
     */
    private UserResultData getUserResultData(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNum(user.getPhoneNum())
                .role(user.getRole())
                .stack(user.getStack())
                .point(user.getPoint())
                .blogAddr(user.getBlogAddr())
                .gitNickname(user.getGitNickname())
                .create_at(user.getCreateAt())
                .update_at(user.getUpdateAt())
                .build();
    }
}
