package com.devthink.devthink_server.application;
import com.devthink.devthink_server.domain.Letter;
import com.devthink.devthink_server.dto.LetterAddData;
import com.devthink.devthink_server.dto.LetterModificationData;
import com.devthink.devthink_server.infra.LetterRepository;
import com.devthink.devthink_server.infra.UserRepository;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 사용자의 요청을 받아, 실제 내부에서 작동하는 클래스 입니다.
 */
@Transactional
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    private final Mapper mapper;

    public LetterService(LetterRepository letterRepository,UserRepository userRepository, Mapper mapper) {
        this.letterRepository = letterRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    /**
     * 사용자로부터 메시지 정보를 받아 메시지 리스트에서 메시지를 보냅니다.
             * @param dto 사용자 메시지 입력 데이터
             * @return Letter 사용자가 보낸 메시지
             */
        public Letter addMessage(LetterAddData dto){

        // RoomId가 0인 경우 프로필에서 보낸 것이고, 0이 아니면 메시지 리스트에서 보낸 것으로 구분
        if(dto.getRoomId() == 0){
            int exist_chat = letterRepository.existChat(dto.getTargetId(), dto.getSenderId());
            // 프로필에서 보낸 메시지중 메시지 내역이 없어서 첫 메시지가 될 경우 구분
            if(exist_chat == 0){
                // 메시지 DB에서 room_id의 최댓값을 가져옵니다.
                Long max_room = letterRepository.maxRoom();
                if(max_room == null)    // room_id 기본 값은 1로 설정합니다.
                    dto.changeRoomId(1L);
                else
                    dto.changeRoomId(max_room+1);   // (room_id의 최댓값 + 1)을 유저가 주고받은 방 번호로 설정합니다.
            }
            else    // 기존에 메시지 내역이 있다면 해당 방 번호를 가져옵니다.
            {
                List<Letter> letters = letterRepository.selectRoom(dto.getTargetId(), dto.getSenderId(), PageRequest.of(0, 1));
                dto.changeRoomId(letters.get(0).getRoomId());
            }

        }
        Letter letter = mapper.map(dto, Letter.class);
        return letterRepository.save(letter);

    }

    /**
     * 전달받은 user_id(유저 아이디)를 통해 방 별 메시지 리스트를 불러옵니다.
     * @param user_id 보여주고자 하는 유저 고유 아이디값
     * @return ArrayList<Letter> 메시지 리스트 값 반환
     */
    public ArrayList<LetterModificationData> messageList(Long user_id) {
        // 메시지 리스트에 나타낼 것들: 가장 최근 메시지, 보낸 사람 id
        ArrayList<Letter> list = letterRepository.getMessageList(user_id);
        ArrayList<LetterModificationData> letterList = new ArrayList<>();

        for (Letter letter : list) {
            letterList.add(mapper.map(letter, LetterModificationData.class));
        }

        for (LetterModificationData letter : letterList) {
            // 현재 사용자가 안읽은 메시지의 개수를 가져옵니다.
            Long unread = letterRepository.countUnread(user_id, letter.getRoomId());

            String image = null;
            if(user_id == letter.getSenderId())
            {
                    image = userRepository.findImageUrlById(letter.getTargetId());
            }
            else if(user_id == letter.getTargetId())
            {
                    image = userRepository.findImageUrlById(letter.getSenderId());
            }

            // 읽지 않은 메시지 갯수를 letter에 change 합니다.
            letter.change(unread);
            letter.setProfile(image);


            // 메시지 유저 아이디를 기준으로 상대 id를 세팅합니다.
            if(user_id == letter.getSenderId()) {
                letter.changeOtherId(letter.getTargetId());
            }
            else{
                letter.changeOtherId(letter.getSenderId());
            }
        }
        return letterList;
    }

    /**
     * 전달 받은 유저의 방 별 메시지 내용을 가져오고, 조회수를 업데이트 합니다.
     * @param user_id 조회하고자 하는 유저 id
     *        room_id 조회하고자 하는 유저의 방 id
     * @return ArrayList<Letter> 메시지 리스트 값 반환
     */
    public ArrayList<LetterModificationData> roomContentList(Long user_id, Long room_id)
    {
        ArrayList<Letter> roomLists = letterRepository.getRoomLists(user_id, room_id);
        ArrayList<LetterModificationData> LetterLists = new ArrayList<>();

        for (Letter roomList : roomLists) {
            LetterLists.add(mapper.map(roomList, LetterModificationData.class));

        }
        // 해당 방에 존재하는 메시지중 받는 사람이 user_id 인 메시지를 읽음 처리 하고, 메시지 확인 시각을 업데이트 합니다.
        LocalDateTime date = LocalDateTime.now();
        letterRepository.MessageReadCheck(user_id, room_id, date);
        return LetterLists;
    }
}