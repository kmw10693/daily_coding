package com.devthink.devthink_server.service;

import com.devthink.devthink_server.domain.Category;
import com.devthink.devthink_server.dto.CategoryDto;
import com.devthink.devthink_server.errors.CategoryNotFoundException;
import com.devthink.devthink_server.infra.CategoryRepository;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 사용자의 요청을 받아, 실제 내부에서 작동하는 클래스 입니다.
 */

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    public CategoryService(CategoryRepository categoryRepository, Mapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    /**
     * 전달받은 게시글 데이터로 새로운 게시글을 DB에 저장합니다.
     * @param categoryDto 게시글 데이터
     * @return  카테고리의 정보를 DB에 저장.
     */
    public Category savePost(CategoryDto categoryDto){
        Category category = mapper.map(categoryDto, Category.class);
        return categoryRepository.save(category);
    }

    /**
     * 전달받은 카테고리의 식별자를 이용하여 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * @param id 찾고자 하는 게시글의 식별자
     * @return 찾았을 경우 게시글을 반환, 찾지 못하면 error를 반환.
     */
    public Category getCategory(Long id)
    {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

    }

    /**
     * 전달받은 카테코리의 식별자와 수정하고자 하는 게시글의 내용을 이용하여 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * 있으면 게시글을 수정하여 DB에 저장합니다.
     * @param id 찾고자 하는 카테고리의 식별자
     * @param categoryDto 수정하고자 하는 카테고리의 내용
     * @return 찾았을 경우 게시글을 반환, 찾지 못하면 error를 반환.
     */
    public Category update(Long id, CategoryDto categoryDto)
    {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        category.update(categoryDto.getName());
        return categoryRepository.save(category);
    }

    /**
     * 전달받은 카테고리의 식별자를 이용하여 게시글을 DB에 찾고, 없으면 Error를 보냅니다.
     * @param id 삭제하고자 하는 게시글의 식별자
     * @return 찾았을 경우 삭제한 게시글 반환, 찾지 못하면 error를 반환
     */
    public Category deletePost(Long id)
    {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        categoryRepository.deleteById(id);
        return category;
    }

}
