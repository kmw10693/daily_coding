package com.devthink.devthink_server.service;

import com.devthink.devthink_server.application.PostService;
import com.devthink.devthink_server.domain.Post;
import com.devthink.devthink_server.dto.PostRequestData;
import com.devthink.devthink_server.errors.PostNotFoundException;
import com.devthink.devthink_server.infra.PostRepository;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostServiceTest {

    private final Long NOT_EXISTED_ID = 2L;

    private PostService postService;
    private PostRepository postRepository = mock(PostRepository.class);

    @BeforeEach
    void setup(){
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        postService = new PostService(postRepository, mapper);

        given(postRepository.save(any(Post.class))).will(invocation -> {
            Post post = Post.builder()
                    .id(1L)
                    .user_id(1L)
                    .category_id(1L)
                    .title("test")
                    .content("test")
                    .status("active")
                    .build();
            return post;
        });


        given(postRepository.findById(1L)).willReturn(
                Optional.of(Post.builder()
                        .id(1L)
                        .user_id(1L)
                        .build())
        );

        given(postRepository.findById(NOT_EXISTED_ID))
                .willThrow(new PostNotFoundException(NOT_EXISTED_ID));

    }

    @Test
    void 올바른_정보로_글을_쓰려는_경우() {
        PostRequestData postRequestData = PostRequestData.builder()
                .user_id(1L)
                .category_id(1L)
                .title("테스트")
                .content("테스트")
                .build();

        Post post = postService.savePost(postRequestData);

        assertThat(post.getUser_id()).isEqualTo(1L);
        assertThat(post.getStatus()).isEqualTo("active");
        verify(postRepository).save(any(Post.class));

    }

    @Test
    void 올바른_정보로_글을_수정하려는_경우(){
        PostRequestData postRequestData = PostRequestData.builder()
                .title("test22")
                .content("test22")
                .build();

        Long user_id =1L;
        Post post = postService.update(user_id , postRequestData);

        assertThat(post.getUser_id()).isEqualTo(1L);
        assertThat(post.getTitle()).isEqualTo("test");
        assertThat(post.getContent()).isEqualTo("test");

        verify(postRepository).findById(1L);
    }

    @Test
    void 존재하지_않는_글을_수정하려는_경우(){
        PostRequestData postRequestData = PostRequestData.builder()
                .title("test22")
                .content("test22")
                .build();

        assertThatThrownBy(() -> postService.update(NOT_EXISTED_ID, postRequestData))
                .isInstanceOf(PostNotFoundException.class);

        verify(postRepository).findById(NOT_EXISTED_ID);
    }

    @Test
    void 존재하는_식별자로_글을_삭제하려는_경우(){
        Post post = postService.deletePost(1L);

        assertThat(post.getId()).isEqualTo(1L);
        verify(postRepository).findById(1L);
    }

    @Test
    void 존재하지_않는_식별자로_글을_삭제하는_경우(){
        assertThatThrownBy(() -> postService.deletePost(NOT_EXISTED_ID))
                .isInstanceOf(PostNotFoundException.class);

    }

}
