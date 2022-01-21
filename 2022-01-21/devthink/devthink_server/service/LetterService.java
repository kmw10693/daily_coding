package com.devthink.devthink_server.service;

import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.dto.LetterAddData;
import com.devthink.devthink_server.infra.LetterRepository;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public void addMessage(LetterAddData dto){


        Letter RecvData = getLetterData(dto, dto.getRecvId());
        Letter SendData = getLetterData(dto, dto.getSendId());

        letterRepository.save(RecvData);
        letterRepository.save(SendData);

    }

    /**
     * 해당 유저 id의 해당 방번호에 해당하는 받은 쪽지, 보낸 쪽지를 조회합니다.
     * @param user_id
     * @param room_id
     * @return
     */
    public List<Letter> getMessage(Long user_id, Long room_id){
        List<Letter> letters = letterRepository.findByUserIdAndRoomIdOrderByIdDesc(user_id, room_id);
        return letters;
    }


    /**
     * 유저의 특정방의 최근쪽지를 조회합니다.
     * @param dto
     * @param user_id
     * @return
     */
    public Letter getRecentSms(Long user_id, Long room_id)
    {
        return letterRepository.
                findFirstByUserIdAndRoomIdOrderByIdDesc(user_id, room_id);
    }

    /**
     * 유저의 roomid의 최댓값을 불러옵니다.
     */
    public Long getMaxRoomId(Long user_id)
    {
        return letterRepository.getMaxRoomId(user_id);
    }

    private Letter getLetterData(LetterAddData dto, Long user_id)
    {
        if(dto == null)
            return null;

        return Letter.builder()
                .userId(user_id)
                .roomId(dto.getRoomId())
                .content(dto.getContent())
                .build();

    }



}