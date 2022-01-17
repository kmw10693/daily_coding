package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.dto.PostDto;
import com.devthink.devthink_server.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/posts")

/**
 * 사용자의 HTTP 요청을 처리하는 클래스입니다.
 */

// 1. 글 작성 -> POST /posts/write
// 2. 글 id로 검색 -> GET /posts/{id}
// 3. 글 업데이트 -> PUT /posts/{id}
// 4. 글 삭제 -> DELETE /posts/{id}
// 5. 페이지 가져오기 -> GET /posts?page=숫자
// ----> 기본 1페이지, 6개의 게시글 가져오기, 최신 날짜순이 기본
// 6. 키워드로 제목 검색 -> GET /posts/search?keyword=문자열
// ----> 문자열이 제목인 게시글 가져오기

public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 게시글에서 페이지를 요청하면 몇 페이지의 게시글을 가져옵니다.
     * @param page 사용자가 요청한 페이지 수
     * @return 몇 페이지의 게시글 (기본값 6개의 게시글)
     */
    @GetMapping
    public List<PostDto> list(@RequestParam(value="page", defaultValue = "1") int page){
        List<Post> list = postService.list(page);
        return getPostDtos(list);
    }

    /**
     * 게시글의 id를 검색하여 게시글을 가져옵니다.
     * @param id 게시글의 고유 id값
     * @return id의 게시글
     */
    @GetMapping("/{id}")
    public PostDto findById(@PathVariable Long id){
        Post post = postService.getPost(id);
        return getPostData(post);

    }

    /**
     * 입력한 게시글의 정보(유저 id, 카테고리 id, 제목, 내용, status)를 입력받아 게시글을 생성합니다.
     * @param postDto 입력한 vaild한 게시글의 정보
     * @return  게시글 생성
     */
    @PostMapping("/write")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto write(@RequestBody @Valid PostDto postDto){
        Post post = postService.savePost(postDto);
        return getPostData(post);
    }

    /**
     * 입력한 게시글의 식별자 값과 valid한 게시글의 정보를 받아, 기존의 게시글을 입력한 정보로 변경합니다.
     * @param id 게시글의 식별자
     * @param postDto 제목, 내용, status
     * @return 기존 게시글의 정보 수정
     */

    @PutMapping("/{id}")
    public PostDto update(@PathVariable Long id, @RequestBody @Valid PostDto postDto){
        Post update = postService.update(id, postDto);
        return getPostData(update);
    }

    /**
     * 입력한 게시글의 식별자 값을 받아 게시글을 삭제합니다.
     * @param id 게시글의 식별자
     */
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id)
    {
        postService.deletePost(id);
    }

    /**
     * keyword를 검색하여, keyword가 담긴 제목의 게시글을 탐색합니다.
     * @param keyword 검색하는 제목
     * @return keyword가 담긴 제목의 게시글
     */

    @GetMapping("/search")
    public List<PostDto> search(@RequestParam String keyword)
    {
        List<Post> search = postService.search(keyword);
        List<PostDto> postDto = getPostDtos(search);
        return postDto;
    }

    /**
     * id를 검색하여, id가 검색된 게시글의 조회수를 1 추가합니다.
     * @param id 게시글의 id값
     * @return id가 담긴 게시글의 정보
     */
    @GetMapping("/read/{id}")
    public PostDto read(@PathVariable Long id)
    {
        Post post = postService.getPost(id);
        postService.updateView(id);
        return getPostData(post);
    }

    /**
     * entity List를 받아 dto List 데이터로 변환하여 반환합니다.
     * @param posts entity List
     * @return 입력된 dto 데이터로 변환된 list
     */
    private List<PostDto> getPostDtos(List<Post> posts) {
        List<PostDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            postDtos.add(getPostData(post));
        }
        return postDtos;
    }


    /**
     * 게시글의 정보를 받아 게시글을 dto 데이터로 변환하여 반환합니다.
     * @param post 게시글 정보
     * @return 입력된 dto 데이터로 변환된 값
     */
    private PostDto getPostData(Post post)
    {
        if(post == null)
            return null;

            return PostDto.builder()
                .user_id(post.getUser_id())
                .category_id(post.getCategory_id())
                .title(post.getTitle())
                    .hit(post.getHit())
                    .status(post.getStatus())
                .content(post.getContent())
                .build();

    }
}
