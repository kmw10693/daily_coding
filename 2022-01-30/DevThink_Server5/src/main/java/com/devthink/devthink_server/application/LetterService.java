package com.devthink.devthink_server.application;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.domain.UserRoom;
import com.devthink.devthink_server.dto.LetterResultData;
import com.devthink.devthink_server.dto.LetterSendData;
import com.devthink.devthink_server.dto.LetterListData;
import com.devthink.devthink_server.infra.LetterRepository;
import com.devthink.devthink_server.infra.UserRepository;
import com.devthink.devthink_server.infra.UserRoomRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자의 요청을 받아, 실제 내부에서 작동하는 클래스 입니다.
 */

@Transactional
@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    /**
     * 메시지 정보를 받아 메시지를 저장합니다.
     *
     * @param
     * @return Letter 사용자가 보낸 메시지
     */
    public Letter addMessage(UserRoom userRoom, User sender, User target, LetterSendData data) {
        Letter letter = letterRepository.save(
                Letter.builder()
                        .content(data.getContent())
                        .heart(data.getHeart())
                        .sender(sender)
                        .target(target)
                        .room(userRoom)
                        .build()
        );
        return letter;
    }

    /**
     * 전달받은 유저 아이디의 방 메시지 리스트를 불러옵니다.
     *
     * @param userId 유저 아이디
     * @return List<Letter> 메시지 리스트
     */
    public List<LetterListData> messageList(Long userId) {
        List<Letter> list = letterRepository.getMessageList(userId);
        List<LetterListData> letterListData = new ArrayList<>();

        for (Letter letter : list) {
            letterListData.add(
                    LetterListData.builder()
                            .roomId(letter.getRoom().getRoomId())
                            .senderId(letter.getSender().getId())
                            .targetId(letter.getTarget().getId())
                            .content(letter.getContent())
                            .readCheck(letter.isReadCheck())
                            .heart(letter.isHeart())
                            .createAt(letter.getCreateAt())
                            .build()
            );
        }
        for (LetterListData letter : letterListData) {
            Long unread = letterRepository.countUnread(userId, letter.getRoomId());
            letter.changeUnRead(unread);

            String image = null;
            String Nickname = null;
            if (userId == letter.getSenderId()) {
                image = userRepository.findImageUrlById(letter.getTargetId());
                Nickname = userRepository.findNicknameById(letter.getTargetId());
            } else if (userId == letter.getTargetId()) {
                image = userRepository.findImageUrlById(letter.getSenderId());
                Nickname = userRepository.findNicknameById(letter.getSenderId());
            }
            letter.setProfile(image);
            letter.setOtherNick(Nickname);

            if (userId == letter.getSenderId()) {
                letter.setOtherId(letter.getTargetId());
            } else {
                letter.setOtherId(letter.getSenderId());
            }
        }
        return letterListData;
    }

    /**
     * 전달 받은 유저의 방 별 메시지 내용을 가져오고, 조회수를 업데이트 합니다.
     *
     * @param userId 조회하고자 하는 유저 id
     *                room_id 조회하고자 하는 유저의 방 id
     * @return ArrayList<Letter> 메시지 리스트 값 반환
     */
    public List<LetterResultData> roomContentList(Long userId, Long roomId) {
        LocalDateTime date = LocalDateTime.now();
        letterRepository.updateRead(userId, roomId, date);

        List<Letter> roomLists = letterRepository.getRoomLists(userId, roomId);
        List<LetterResultData> LetterLists = new ArrayList<>();

        for (Letter roomList : roomLists) {
            LetterLists.add(
                    LetterResultData.builder()
                            .roomId(roomList.getRoom().getRoomId())
                            .sendNick(roomList.getSender().getNickname())
                            .targetNick(roomList.getTarget().getNickname())
                            .heart(roomList.isHeart())
                            .targetId(roomList.getTarget().getId())
                            .senderId(roomList.getSender().getId())
                            .content(roomList.getContent())
                            .createAt(roomList.getCreateAt())
                            .viewAt(roomList.getViewAt())
                            .readCheck(roomList.isReadCheck())
                            .build()
            );
        }
        return LetterLists;
    }
}