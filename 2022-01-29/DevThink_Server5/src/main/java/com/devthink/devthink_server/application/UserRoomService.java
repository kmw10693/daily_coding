package com.devthink.devthink_server.application;

import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.domain.UserRoom;
import com.devthink.devthink_server.dto.UserRegistrationData;
import com.devthink.devthink_server.errors.UserEmailDuplicationException;
import com.devthink.devthink_server.errors.UserNickNameDuplicationException;
import com.devthink.devthink_server.errors.UserNotFoundException;
import com.devthink.devthink_server.errors.UserRoomNotFoundException;
import com.devthink.devthink_server.infra.UserRoomRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoomService {

    private final UserRoomRepository userRoomRepository;
    private final Mapper mapper;

    public Optional<UserRoom> getUserRoom(Long userId, Long roomId) {
        return userRoomRepository.getUserRoom(userId, roomId);
    }

    public UserRoom save(UserRegistrationData userRegistrationData) {
        User user = mapper.map(userRegistrationData, User.class);
        return userRepository.save(user);
    }

    public Long getMaxRoom(){
        return userRoomRepository.maxRoom();
    }
}
