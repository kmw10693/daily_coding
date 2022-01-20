package com.devthink.devthink_server.service;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.dto.LetterDto;
import com.devthink.devthink_server.errors.PostNotFoundException;
import com.devthink.devthink_server.infra.LetterRepository;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final Mapper mapper;

    public LetterService(LetterRepository letterRepository, Mapper mapper) {
        this.letterRepository = letterRepository;
        this.mapper = mapper;
    }

    /**
     * 메시지 작성
     * @param dto
     */
    public void addMessage(LetterDto dto){
        Letter letter = mapper.map(dto, Letter.class);
        letterRepository.save(letter);

    }

    /**
     * 해당 유저 id의 해당 방번호에 해당하는 받은 쪽지, 보낸 쪽지를 조회합니다.
     * @param user_id
     * @param room_id
     * @return
     */
    public List<Letter> getMessage(Long user_id, Long room_id){
        List<Letter> letters = letterRepository.findView(user_id, room_id);
        return letters;
    }
}