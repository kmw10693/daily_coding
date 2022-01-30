package com.devthink.devthink_server.application;

import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.infra.UserRepository;
import com.devthink.devthink_server.dto.UserModificationData;
import com.devthink.devthink_server.dto.UserRegistrationData;
import com.devthink.devthink_server.errors.UserEmailDuplicationException;
import com.devthink.devthink_server.errors.UserNickNameDuplicationException;
import com.devthink.devthink_server.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 사용자의 요청을 받아, 실제 내부에서 작동하는 클래스 입니다.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;


    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    /**
     * 전달받은 사용자 식별자를 통해 사용자를 조회합니다.
     * @param id 사용자 식별자
     * @return 식별자와 일치하는 식별자를 가진 사용자.
     */
    public User getUser(Long id) {
        return findUser(id);
    }

    /**
     * 전달받은 사용자의 가입 데이터로 새로운 사용자의 정보를 DB에 저장합니다.
     * @param userRegistrationData 사용자의 가입 데이터
     * @return 사용자의 정보를 DB에 저장.
     */
    public User registerUser(UserRegistrationData userRegistrationData) {
        String email = userRegistrationData.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }
        String nickname = userRegistrationData.getNickname();
        if(userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicationException(nickname);
        }
        User user = mapper.map(userRegistrationData, User.class);
        return userRepository.save(user);
    }

    /**
     * 전달받은 사용자 email과 같은 email이 DB에 존재하는지 확인합니다.
     * @param userEmail 전달받은 사용자 email
     * @return DB에 존재 여부. 존재하면 exception, 존재하지 않으면 false를 반환합니다.
     */
    public Boolean isDuplicateEmail(String userEmail) {
        if(userRepository.existsByEmail(userEmail)) {
            throw new UserEmailDuplicationException(userEmail);
        } else {
            return false;
        }
    }

    /**
     * 전달받은 사용자 userNickName과 같은 userNickName이 DB에 존재하는지 확인합니다.
     * @param userNickName 전달받은 사용자의 userNickName
     * @return DB에 존재 여부. 존재하면 exception, 존재하지 않으면 false를 반환합니다.
     */
    public Boolean isDuplicateNickname(String userNickName) {
        if(userRepository.existsByNickname(userNickName)) {
            throw new UserNickNameDuplicationException(userNickName);
        } else {
            return false;
        }
    }

    /**
     * 수정할 사용자 식별자와, 수정할 사용자의 정보를 받아 사용자 정보를 수정하여 수정된 사용자를 반환합니다.
     * @param id 수정할 사용자 식별자
     * @param modificationData 수정할 사용자의 정보
     * @return 수정된 사용자.
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 전달받은 사용자의 식별자를 이용하여 사용자를 DB에서 찾고, 없으면 Error를 보냅니다.
     * @param id 찾고자 하는 사용자의 식별자
     * @return 찾았을 경우 사용자를 반환. 찾지 못했을 경우 error를 반환.
     */
    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    /**
     * 삭제하고자 하는 사용자의 식별자를 전달받아, 사용자를 찾아 삭제하고, 사용자를 반환합니다.
     * @param id 삭제하고자 하는 사용자의 식별자
     * @return 삭제된 사용자.
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }


}
