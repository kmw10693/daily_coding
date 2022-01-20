package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.dto.LetterDto;
import com.devthink.devthink_server.dto.PostDto;
import com.devthink.devthink_server.service.LetterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


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
     * @param letterdto
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addMessage(@RequestBody @Valid LetterDto letterdto){
        ResponseEntity<String> entity = null;
        try {
            letterService.addMessage(letterdto);
            entity = new ResponseEntity<String>("쪽지를 성공적으로 보냈습니다.", HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        return entity;
    }

    /**
     * 유저 방에 있는 보낸 쪽지와 받은 쪽지 모두 확인
     */
    @GetMapping
    public List<LetterDto> getMessage(@RequestParam Long user_id, @RequestParam Long room_id)
    {
        List<Letter> messages = letterService.getMessage(user_id, room_id);
        return getLetterDtos(messages);
    }

    /**
     * List<letter>를 List<LetterDto>로 변환합니다.
     * @param letters
     * @return
     */
    private List<LetterDto> getLetterDtos(List<Letter> letters) {
        List<LetterDto> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            letterDtos.add(getLetterData(letter));

        }
        return letterDtos;
    }

    /**
     * 게시글의 정보를 받아 게시글을 dto 데이터로 변환하여 반환합니다.
     * @return 입력된 dto 데이터로 변환된 값
     */
    private LetterDto getLetterData(Letter letter)
    {
        if(letter == null)
            return null;

        return LetterDto.builder()
                .roomId(letter.getRoomId())
                .sendId(letter.getSendId())
                .recvId(letter.getRecvId())
                .content(letter.getContent())
                .create_at(letter.getCreate_at())
                .build();

    }
}