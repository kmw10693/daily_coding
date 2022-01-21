package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.UserRoom;
import com.devthink.devthink_server.dto.LetterAddData;
import com.devthink.devthink_server.dto.LetterResultData;
import com.devthink.devthink_server.dto.UserRoomResultData;
import com.devthink.devthink_server.service.LetterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// 메시지 전송 코드
@RestController
@RequestMapping("/messages")
public class LetterController {

    private LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    /**
     * 쪽지 보내기 기능
     * @param
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addMessage(@RequestBody @Valid LetterAddData letterAddData){
        ResponseEntity<String> entity = null;
        try {
            letterService.addMessage(letterAddData);
            entity = new ResponseEntity<String>("쪽지를 성공적으로 보냈습니다.", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        return entity;
    }

    /**
     * 유저 방에 있는 보낸 쪽지와 받은 쪽지 정렬하여 모두 확인
     */
    @GetMapping
    public List<LetterResultData> getMessage(@RequestParam Long user_id, @RequestParam Long room_id)
    {
        List<Letter> messages = letterService.getMessage(user_id, room_id);
        return getLetterDtos(messages);
    }

    /**
     * 유저 쪽지방을 모두 확인합니다.
     * @param
     * @return
     */
    @GetMapping("/{user_id}")
    public List<UserRoomResultData> getList(@PathVariable Long user_id)
    {
        Long max_roomId = letterService.getMaxRoomId(user_id);
        List<Letter> messages = new ArrayList<>();

        for(Long i=1L; i<=max_roomId; i++) {
            Letter recentSms = letterService.getRecentSms(user_id, i);
            messages.add(recentSms);
        }
        List<UserRoomResultData> userRoomDtos = getUserRoomDtos(messages);
        return userRoomDtos;

    }

    private List<LetterResultData> getLetterDtos(List<Letter> letters) {
        List<LetterResultData> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            letterDtos.add(getLetterData(letter));

        }
        return letterDtos;
    }

    /**
     * 게시글의 정보를 받아 게시글을 dto 데이터로 변환하여 반환합니다.
     * @return 입력된 dto 데이터로 변환된 값
     */
    private LetterResultData getLetterData(Letter letter)
    {
        if(letter == null)
            return null;

        return LetterResultData.builder()
                .content(letter.getContent())
                .create_at(letter.getCreate_at())
                .read_chk(letter.getRead_chk())
                .room_id(letter.getRoomId())
                .user_id(letter.getUserId())
                .view_at(letter.getView_at())
                .build();

    }

    private List<UserRoomResultData> getUserRoomDtos(List<Letter> letters) {
        List<UserRoomResultData> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            letterDtos.add(getUserRoomData(letter));

        }
        return letterDtos;
    }

    private UserRoomResultData getUserRoomData(Letter letter)
    {
        if(letter == null)
            return null;

        return UserRoomResultData.builder()
                .recentSms(letter.getContent())
                .room_id(letter.getRoomId())
                .create_at(letter.getCreate_at())
                .build();
    }
}