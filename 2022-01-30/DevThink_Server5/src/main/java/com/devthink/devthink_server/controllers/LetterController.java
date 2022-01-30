package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.application.LetterService;
import com.devthink.devthink_server.application.UserRoomService;
import com.devthink.devthink_server.application.UserService;
import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.domain.UserRoom;
import com.devthink.devthink_server.dto.LetterListData;
import com.devthink.devthink_server.dto.LetterSendData;
import com.devthink.devthink_server.dto.LetterResultData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 사용자의 HTTP 요청을 처리하는 클래스입니다.
 */

// 1. 쪽지 보내기  -> POST /messages
// 2. 사용자 id로 방별 메시지 리스트 검색 -> GET /messages?user_id=숫자
// ---> unread : 안 읽은 메시지 갯수
// 3. 사용자 id와 방 id로 메시지 읽기 -> GET /messages/rooms?user_id=숫자&room_id=숫자
// ---> readCheck 기본 값 0 : 안 읽은 경우, 1로 갱신하여 읽음 처리

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;
    private final UserService userService;
    private final UserRoomService userRoomService;

    /**
     * 쪽지 정보를 받아 리스트에서 쪽지를 보냅니다.
     *
     * @param letterAddData 쪽지를 보내기 위한 데이터
     * @return LetterResultData 쪽지 성공 여부
     */
    @PostMapping
    @ApiOperation(value = "메시지 전송",
            notes = "사용자로부터 메시지 정보를 받아 메시지 리스트에서 쪽지를 보냅니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public LetterResultData messageSendList(@RequestBody @Valid LetterSendData letterAddData) {
        User sender = userService.getUser(letterAddData.getSenderId());
        User target = userService.getUser(letterAddData.getTargetId());
        UserRoom userRoom;

        if (letterAddData.getRoomId() == 0) {
            int existRoom = userRoomService.existChat(sender.getId(), target.getId());

            if (existRoom == 0) {
                Long maxRoom = userRoomService.getMaxRoom();

                if (maxRoom == null)
                    userRoom = userRoomService.save(sender, target, 1L);
                else
                    userRoom = userRoomService.save(sender, target, maxRoom + 1);

            } else {
                userRoom = userRoomService.getExistUserRoom(sender.getId(), target.getId());
            }
        } else {
            userRoom = userRoomService.getUserRoom(sender.getId(), target.getId(), letterAddData.getRoomId());
        }
        Letter letter = letterService.addMessage(userRoom, sender, target, letterAddData);
        return getLetterData(letter);
    }

    /**
     * 유저 id를 받아 메시지 리스트를 반환합니다.
     *
     * @param userId 고유 id 값
     * @return List<LetterResultData> 메시지 리스트
     */
    @GetMapping
    @ApiOperation(value = "메시지 리스트",
            notes = "유저 고유 id를 받아 방 리스트를 반환합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Long", value = "유저 고유 아이디")})
    public List<LetterListData> messageList(@RequestParam Long userId) {
        return letterService.messageList(userId);
    }

    /**
     * 사용자로부터 유저 id와 방 id를 받아 메시지 내용을 가져옵니다.
     * @param userId 사용자의 고유 id 값, room_id 사용자의 방 번호
     * @return List<LetterResultData> 사용자의 방 번호에서 주고받은 메시지
     */
    @GetMapping("/rooms")
    @ApiOperation(value = "메시지 내용 가져오기",
            notes = "사용자로부터 유저 고유 id와 방 id를 받아 메시지 내용을 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Long", value = "유저 아이디"),
            @ApiImplicitParam(name = "roomId", dataType = "Long", value = "방 아이디")
    })
    public List<LetterResultData> MessageRoomList(@RequestParam Long userId, @RequestParam Long roomId) {
        List<LetterResultData> letters = letterService.roomContentList(userId, roomId);
        return letters;
    }

    /**
     * 쪽지의 정보를 받아 쪽지를 dto 데이터(LetterResultData)로 변환하여 반환합니다.
     *
     * @param letter 쪽지 정보
     * @return 입력된 dto 데이터(LetterResultData)로 변환된 값
     */
    private LetterResultData getLetterData(Letter letter) {
        if (letter == null)
            return null;

        return LetterResultData.builder()
                .sendNick(letter.getSender().getNickname())
                .targetNick(letter.getTarget().getNickname())
                .content(letter.getContent())
                .readCheck(letter.isReadCheck())
                .roomId(letter.getRoom().getRoomId())
                .senderId(letter.getSender().getId())
                .targetId(letter.getTarget().getId())
                .createAt(letter.getCreateAt())
                .viewAt(letter.getViewAt())
                .heart(letter.isHeart())
                .build();

    }
}