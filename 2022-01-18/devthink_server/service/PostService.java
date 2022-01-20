package com.devthink.devthink_server.service;

import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.dto.PostDto;
import com.devthink.devthink_server.errors.PostNotFoundException;
import com.devthink.devthink_server.infra.PostRepository;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자의 요청을 받아, 실제 내부에서 작동하는 클래스 입니다.
 */
@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final Mapper mapper;

    public PostService(PostRepository postRepository, Mapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    /**
     * 전달받은 게시글 데이터로 새로운 게시글을 DB에 저장합니다.
     * @param postDto 게시글 데이터
     * @return  사용자의 정보를 DB에 저장.
     */
    public Post savePost(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        return postRepository.save(post);
    }

    /**
     * 전체 게시글을 조회합니다.
     * @return 전체 게시글
     */
    public List<Post> getPostList(){
        return postRepository.findAll();
    }

    /**
     * 전달받은 page를 받아 page에 해당하는 게시글을 반환합니다.
     * -----> 기본 : 페이지당 6개의 게시글
     * @param page 얻고자 하는 page
     * @return  page에 해당하는 게시글
     */
    public List<Post> list(int page){
        Page<Post> pageList = postRepository.findAll(PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.DESC, "id")));
        return pageList.getContent();
    }

    /**
     * 전달받은 게시글의 식별자를 이용하여 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * @param id 찾고자 하는 게시글의 식별자
     * @return 찾았을 경우 게시글을 반환, 찾지 못하면 error를 반환.
     */
    public Post getPost(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

    }

    /**
     * 전달받은 게시글의 식별자와 수정하고자 하는 게시글의 내용을 이용하여 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * 있으면 게시글을 수정하여 DB에 저장합니다.
     * @param id 찾고자 하는 게시글의 식별자
     * @param postDto 수정하고자 하는 게시글의 내용
     * @return 찾았을 경우 게시글을 반환, 찾지 못하면 error를 반환.
     */

    public Post update(Long id, PostDto postDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        post.update(postDto.getTitle(), postDto.getContent());
        return postRepository.save(post);
    }

    /**
     * 전달받은 게시글의 식별자를 이용하여 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * @param id 삭제하고자 하는 게시글의 식별자
     * @return 찾았을 경우 삭제한 게시글 반환, 찾지 못하면 error를 반환
     */
    public Post deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.deleteById(id);
        return post;
    }

    /**
     * 전달받은 키워드를 이용하여 제목에 키워드가 담긴 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * @param keyword 찾고자 하는 제목
     * @return 찾았을 경우 게시글 반환, 찾지 못하면 error를 반환
     */
    public List<Post> search(String keyword)
    {
        List<Post> postList = postRepository.findByTitleContaining(keyword);
        return postList;
    }

    /**
     * 전달받은 id를 통하여 게시글을 찾고, 조회수를 1 추가합니다.
     * @param id 찾고자 하는 게시글의 id값
     * @return 찾았을 경우 게시글을 반환, 찾지 못하면 error를 반환
     */
    public Post updateView(Long id)
    {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRepository.updateView(id);
        return post;
    }
}
