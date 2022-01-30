package com.devthink.devthink_server.application;

import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.infra.UserRepository;
import com.devthink.devthink_server.dto.UserModificationData;
import com.devthink.devthink_server.dto.UserRegistrationData;
import com.devthink.devthink_server.errors.UserEmailDuplicationException;
import com.devthink.devthink_server.errors.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class UserServiceTest {
    private final String EXISTED_EMAIL = "exited@email.com";
    private final Long NOT_EXISTED_ID = 1000L;
    private final Long DELETED_ID = 200L;

    private UserService userService;

    private final UserRepository userRepository = mock(UserRepository.class);


    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        userService = new UserService(userRepository, mapper);

        given(userRepository.save(any(User.class))).will(invocation -> {
            User user = User.builder()
                    .id(13L)
                    .email(EXISTED_EMAIL)
                    .nickname("Test")
                    .role("senior")
                    .build();

            return user;
        });

        given(userRepository.findByIdAndDeletedIsFalse(1L)).willReturn(
                Optional.of(User.builder()
                        .id(1L)
                        .email(EXISTED_EMAIL)
                        .build())
        );
        
        given(userRepository.existsByEmail(EXISTED_EMAIL))
                .willThrow(new UserEmailDuplicationException(EXISTED_EMAIL));

        given(userRepository.findByIdAndDeletedIsFalse(NOT_EXISTED_ID))
                .willReturn(Optional.empty());
        
        given(userRepository.findByIdAndDeletedIsFalse(DELETED_ID))
                .willReturn(Optional.empty());
        
    }

    @Test
    void 올바른_정보로_회원가입을_하려는_경우() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .nickname("tester")
                .email("test@email.com")
                .build();

        User user = userService.registerUser(registrationData);

        assertThat(user.getId()).isEqualTo(13L);
        assertThat(user.getNickname()).isEqualTo("Test");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void 올바른_정보로_사용자정보를_수정하려는_경우() {
        UserModificationData modificationData = UserModificationData.builder()
                .nickname("test")
                .role("senior")
                .build();
        Long userId = 1L;
        User user = userService.updateUser(userId, modificationData);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getNickname()).isEqualTo("test");
        assertThat(user.getEmail()).isEqualTo(EXISTED_EMAIL);
        assertThat(user.getRole()).isEqualTo("senior");

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void 존재하지_않는_사용자정보를_수정하려는_경우() {
        UserModificationData modificationData = UserModificationData.builder()
                .nickname("test")
                .role("senior")
                .build();

        assertThatThrownBy(() ->userService.updateUser(NOT_EXISTED_ID, modificationData))
                .isInstanceOf(UserNotFoundException.class);
        
        verify(userRepository).findByIdAndDeletedIsFalse(NOT_EXISTED_ID);
    }

    @Test
    void 삭제된_사용자정보를_수정하려는_경우() {
        UserModificationData modificationData = UserModificationData.builder()
                .nickname("test")
                .role("senior")
                .build();
        Long userId = DELETED_ID;
        assertThatThrownBy(
                () -> userService.updateUser(userId, modificationData)
        )
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 존재하는_식별자로_사용자를_삭제하려는_경우() {
        User user = userService.deleteUser(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.isDeleted()).isTrue();

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void 존재하지_않는_식별자로_사용자를_삭제하려는_경우() {
        assertThatThrownBy(() -> userService.deleteUser(DELETED_ID))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_ID);
    }
}
