package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.dto.LetterAddData;
import com.devthink.devthink_server.dto.LetterListData;
import com.devthink.devthink_server.dto.LetterResultData;
import com.devthink.devthink_server.dto.PostResultData;
import com.devthink.devthink_server.service.LetterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
public class LetterController {

    private LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    /**
     * 사용자로부터 쪽지 정보를 받아 메시지 리스트에서 쪽지를 보냅니다.
     * @param letterAddData 쪽지를 보내기 위한 데이터
     * @return String 쪽지 성공 여부 문자열로 반환
     */
    @PostMapping
    public LetterResultData messageSendInlist(@RequestBody @Valid LetterAddData letterAddData)
    {
        Letter letter = letterService.addMessage(letterAddData);
        return getLetterData(letter);
    }

    /**
     * 사용자로 부터 유저 id를 받아 방 별 메시지 리스트를 반환합니다.
     * @param user_id 사용자의 고유 id 값
     * @return List<LetterResultData> 메시지 리스트(최근 메시지, 보낸 사람 Id,
     * 해당 room에서 안읽은 메시지 갯수)
     */
    @GetMapping
    public List<LetterListData> messageList(@RequestParam Long user_id)
    {
        ArrayList<Letter> letters = letterService.messageList(user_id);
        return getLetterListDtos(letters);
    }

    /**
     * 사용자로부터 유저 id와 방 id를 받아 메시지 내용을 가져옵니다.
     * @param user_id 사용자의 고유 id 값, room_id 사용자의 방 번호
     * @return List<LetterResultData> 사용자의 방 번호에서 주고받은 메시지
     */
    @GetMapping("/rooms")
    public List<LetterResultData> MessageRoomList(@RequestParam Long user_id, @RequestParam Long room_id)
    {
        ArrayList<Letter> letters = letterService.roomContentList(user_id, room_id);
        return getLetterDtos(letters);

    }


    /**
     * entity List를 받아 dto List 데이터로 변환하여 반환합니다.
     * @param letters entity List
     * @return 입력된 dto 데이터로 변환된 list
     */
    private List<LetterResultData> getLetterDtos(List<Letter> letters) {
        List<LetterResultData> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            letterDtos.add(getLetterData(letter));
        }
        return letterDtos;
    }

    /**
     * entity List를 받아 dto List 데이터로 변환하여 반환합니다.
     * @param letters entity List
     * @return 입력된 dto 데이터로 변환된 list
     */
    private List<LetterListData> getLetterListDtos(List<Letter> letters) {
        List<LetterListData> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            letterDtos.add(getLetterListData(letter));
        }
        return letterDtos;
    }


    /**
     * 쪽지의 정보를 받아 쪽지를 dto 데이터로 변환하여 반환합니다.
     * @param letter 쪽지 정보
     * @return 입력된 dto 데이터로 변환된 값
     */
    private LetterResultData getLetterData(Letter letter)
    {
        if(letter == null)
            return null;

        return LetterResultData.builder()
                .content(letter.getContent())
                .readCheck(letter.getReadCheck())
                .roomId(letter.getRoomId())
                .senderId(letter.getSenderId())
                .targetId(letter.getTargetId())
                .create_at(letter.getCreate_at())
                .build();

    }

    /**
     * 쪽지의 정보를 받아 쪽지를 dto 데이터로 변환하여 반환합니다.
     * @param letter 쪽지 정보
     * @return 입력된 dto 데이터로 변환된 값
     */
    private LetterListData getLetterListData(Letter letter)
    {
        if(letter == null)
            return null;

        return LetterListData.builder()
                .content(letter.getContent())
                .otherId(letter.getOther_id())
                .create_at(letter.getCreate_at())
                .roomId(letter.getRoomId())
                .unread(letter.getUnread())
                .build();

    }

}