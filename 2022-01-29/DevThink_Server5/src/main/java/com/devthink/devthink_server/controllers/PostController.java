package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.application.CategoryService;
import com.devthink.devthink_server.application.PostService;
import com.devthink.devthink_server.application.UserService;
import com.devthink.devthink_server.domain.Category;
import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.domain.User;
import com.devthink.devthink_server.dto.PostRequestData;
import com.devthink.devthink_server.dto.PostResponseData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor

/**
 * 사용자의 HTTP 요청을 처리하는 클래스입니다.
 */

// 1. 글 작성 -> POST /posts/write
// 2. 글 id로 검색 -> GET /posts/{id}
// 3. 글 업데이트 -> PUT /posts/{id}
// 4. 글 삭제 -> DELETE /posts/{id}
// 5. 페이지 가져오기 -> GET /posts?page=숫자
// 6. 키워드로 제목 검색 -> GET /posts/search?keyword=문자열

public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CategoryService categoryService;

    /**
     * 페이지를 요청하면 페이지의 게시글을 가져옵니다.
     * @param page 요청 페이지(기본 값: 1개)
     * @return 페이지의 글(기본 값: 6개)
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 리스트", notes = "사용자가 페이지를 요청하면 해당하는 페이지를 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", example ="1", value = "게시글의 페이지")})
    public List<PostResponseData> list(@RequestParam(value="page", defaultValue = "1") int page){
        List<Post> list = postService.list(page);
        return getPostList(list);
    }

    /**
     * 게시글의 Id를 검색하여 게시글을 반환합니다.
     * @param id 게시글의 Id
     * @return Id인 게시글
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색", notes = "게시글의 id를 검색하여 게시글을 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "Long", value = "게시글 고유 아이디")})
    public PostResponseData findById(@PathVariable Long id){
        Post post = postService.getPost(id);
        return getPostData(post);
    }

    /**
     * 게시글의 정보를 입력받아 게시글을 생성합니다.
     * @param postRequestData 게시글의 정보
     * @return 게시글 생성
     */
    @PostMapping("/write")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "게시글 등록",
            notes = "입력한 게시글의 정보(유저 id, 카테고리 id, 제목, 내용, status)를 입력받아 게시글을 생성합니다.")
    public PostResponseData write(@RequestBody @Valid PostRequestData postRequestData){
        User user = userService.getUser(postRequestData.getUserId());
        Category category = categoryService.getCategory(postRequestData.getCategoryId());

        Post post = postService.savePost(user, category, postRequestData);
        PostResponseData postData = getPostData(post);

        if(post.getImageUrl().isEmpty()){
            postData.setImage(false);
        }
        else{
            postData.setImage(true);
        }
        return postData;
    }

    /**
     * 게시글 식별자 값과 정보를 받아, 게시글을 입력한 정보로 변경합니다.
     * @param id 게시글의 식별자
     * @param postRequestData 제목, 내용
     * @return 게시글 정보 수정
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 수정",
            notes = "입력한 게시글의 식별자 값과 valid한 게시글의 정보를 받아, 기존의 게시글을 입력한 정보로 변경합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "Long", value = "게시글 고유 아이디")})
    public PostResponseData update(@PathVariable Long id, @RequestBody @Valid PostRequestData postRequestData){
        Post update = postService.update(id, postRequestData);
        return getPostData(update);
    }

    /**
     * 게시글의 식별자를 받아 게시글을 삭제합니다.
     * @param id 게시글 식별자
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 삭제", notes = "입력한 게시글의 식별자 값을 받아 게시글을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "Long", value = "게시글 고유 아이디")})
    public void deletePost(@PathVariable Long id){
        postService.deletePost(id);
    }

    /**
     * 키워드를 입력받아, 제목이 담긴 게시글을 반환합니다.
     * @param keyword 검색 키워드
     * @return 제목이 담긴 게시글
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "게시글 검색",
            notes = "사용자로부터 keyword를 받아, 제목이 keyword인 게시글을 반환합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", dataType = "Long", value = "검색하고자 하는 게시글 제목")})
    public List<PostResponseData> search(@RequestParam String keyword){
        List<Post> search = postService.search(keyword);
        List<PostResponseData> postRequestData = getPostList(search);
        return postRequestData;
    }

    /**
     * 베스트 게시글 가져오기 API
     */
    @GetMapping("/best")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "베스트 게시글 가져오기",
            notes = "사용자로부터 카테고리 id를 받아, 베스트 게시글을 가져옵니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", dataType = "Long", value = "카테고리 아이디")})
    public List<PostResponseData> searchBest(@RequestParam Long categoryId){
        Category category = categoryService.getCategory(categoryId);
        List<Post> bestPost = postService.getBestPost(categoryId);
        return getPostList(bestPost);
    }

    /**
     * 게시글을 받아 Response 데이터로 변환하여 반환합니다.
     * @param posts 게시글 list
     * @return Response list
     */
    private List<PostResponseData> getPostList(List<Post> posts) {
        List<PostResponseData> postRequestData = new ArrayList<>();

        for (Post post : posts) {
            postRequestData.add(getPostData(post));
        }
        return postRequestData;
    }

    /**
     * 게시글을 받아 Response 데이터로 변환합니다.
     * @param post 게시글
     * @return Response 데이터
     */
    private PostResponseData getPostData(Post post){
        if(post == null)
            return null;

        return PostResponseData.builder()
                .categoryId(post.getCategory().getId())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .deleted(post.getDeleted())
                .updateAt(post.getUpdateAt())
                .createAt(post.getCreateAt())
                .heart(post.getHeart())
                .id(post.getId())
                .build();
    }
}
