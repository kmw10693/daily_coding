package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.application.LetterService;
import com.devthink.devthink_server.application.UserRoomService;
import com.devthink.devthink_server.application.UserService;
import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.domain.UserRoom;
import com.devthink.devthink_server.dto.LetterSendData;
import com.devthink.devthink_server.dto.LetterListData;
import com.devthink.devthink_server.dto.LetterModificationData;
import com.devthink.devthink_server.dto.LetterResultData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    private LetterService letterService;
    private UserService userService;
    private UserRoomService userRoomService;

    /**
     * 사용자로부터 쪽지 정보를 받아 메시지 리스트에서 쪽지를 보냅니다.
     * @param letterAddData 쪽지를 보내기 위한 데이터
     * @return String 쪽지 성공 여부 문자열로 반환
     */
    @PostMapping
    @ApiOperation(value = "메시지 전송",
            notes = "사용자로부터 메시지 정보를 받아 메시지 리스트에서 쪽지를 보냅니다.")
    public LetterResultData messageSendInlist(@RequestBody @Valid LetterSendData letterAddData) {
        User sender = userService.getUser(letterAddData.getSenderId());
        User target = userService.getUser(letterAddData.getTargetId());

        // 유저 방 없으면 0 보내고
        Optional<UserRoom> userRoom = userRoomService.getUserRoom(letterAddData.getSenderId(), letterAddData.getRoomId());

        if(userRoom.isPresent()){   // 유저 방이 있다면 유저 방으로 보내면 되고
            letterService.addMessage((userRoom, sender, target, letterAddData))
        }
        else{   // 유저 방이 없으면 각 유저 확인하고,
            // 유저 방의 최댓값을 userRoom으로 보내면 되겠지
            if(userRoomService.getMaxRoom() == null){
            }
            else{

            }
        }

        // 유저 방 있으면 유저방 보내면 됨
        Letter letter = letterService.addMessage(sender, target, letterAddData);
        return getLetterData(letter);
    }

    /**
     * 유저 id를 받아 메시지 리스트를 반환합니다.
     * @param userId 고유 id 값
     * @return List<LetterResultData> 메시지 리스트
     */
    @GetMapping
    @ApiOperation(value = "메시지 리스트",
            notes = "사용자로부터 유저 고유 id를 받아 방 별 메시지 리스트를 반환합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", dataType = "Long", value = "유저 고유 아이디")})
    public List<LetterListData> messageList(@RequestParam Long userId) {
        ArrayList<LetterModificationData> letters = letterService.messageList(userId);
        return getLetterLists(letters);
    }

    /**
     * 사용자로부터 유저 id와 방 id를 받아 메시지 내용을 가져옵니다.
     * @param user_id 사용자의 고유 id 값, room_id 사용자의 방 번호
     * @return List<LetterResultData> 사용자의 방 번호에서 주고받은 메시지
     */
    @GetMapping("/rooms")
    @ApiOperation(value = "메시지 내용 가져오기",
            notes = "사용자로부터 유저 고유 id와 방 id를 받아 메시지 내용을 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", dataType = "Long", value = "유저 고유 아이디"),
            @ApiImplicitParam(name = "room_id", dataType = "Long", value = "방 아이디")
    })
    public List<LetterResultData> MessageRoomList(@RequestParam Long user_id, @RequestParam Long room_id) {
        ArrayList<LetterModificationData> letters = letterService.roomContentList(user_id, room_id);
        return getLetterDtos(letters);

    }

    /**
     * LetterModifyData를 받아 LetterListData로 변환합니다.
     * @param letter  변환하고자 하는 데이터
     * @return LetterListData 방 별 리스트 데이터
     */
    private LetterListData getLetterListData(LetterModificationData letter){
        if(letter == null)
            return null;

        return LetterListData.builder()
                .unread(letter.getUnread())
                .otherId(letter.getOtherId())
                .content(letter.getContent())
                .roomId(letter.getRoomId())
                .createAt(letter.getCreateAt())
                .image(letter.getProfile())
                .build();
    }

    /**
     * LetterModifyData List를 받아 LetterListData List 데이터로 변환하여 반환합니다.
     * @param letters 변환하고자 하는 letterModifyData List
     * @return 입력된 LetterListData 데이터로 변환된 list
     */
    private List<LetterListData> getLetterLists(List<LetterModificationData> letters) {
        List<LetterListData> letterDtos = new ArrayList<>();

        for (LetterModificationData letter : letters) {
            letterDtos.add(getLetterListData(letter));
        }
        return letterDtos;
    }


    /**
     * LetterModifyData List를 받아 LetterResultData List 데이터로 변환하여 반환합니다.
     * @param letters LetterModifyData List
     * @return LetterResultData로 변환된 list
     */
    private List<LetterResultData> getLetterDtos(List<LetterModificationData> letters) {
        List<LetterResultData> letterDtos = new ArrayList<>();

        for (LetterModificationData letter : letters) {
            letterDtos.add(getLetterResultData(letter));
        }
        return letterDtos;
    }

    /**
     * 쪽지의 정보를 받아 쪽지를 dto 데이터(LetterResultData)로 변환하여 반환합니다.
     * @param letter 쪽지 정보
     * @return 입력된 dto 데이터(LetterResultData)로 변환된 값
     */
    private LetterResultData getLetterData(Letter letter) {
        if(letter == null)
            return null;

        return LetterResultData.builder()
                .content(letter.getContent())
                .readCheck(letter.getReadCheck())
                .roomId(letter.getRoomId())
                .senderId(letter.getSenderId())
                .targetId(letter.getTargetId())
                .createAt(letter.getCreateAt())
                .viewAt(letter.getViewAt())
                .build();

    }

    /**
     * LetterModifyData를 받아 쪽지를 LetterResult 데이터로 변환하여 반환합니다.
     * @param letter 쪽지 정보
     * @return LetterResult 데이터로 변환된 값
     */
    private LetterResultData getLetterResultData(LetterModificationData letter) {
        if(letter == null)
            return null;

        return LetterResultData.builder()
                .content(letter.getContent())
                .readCheck(letter.getReadCheck())
                .roomId(letter.getRoomId())
                .senderId(letter.getSenderId())
                .targetId(letter.getTargetId())
                .createAt(letter.getCreateAt())
                .viewAt(letter.getViewAt())
                .build();

    }
}