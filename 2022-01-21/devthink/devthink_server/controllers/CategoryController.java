package com.devthink.devthink_server.controllers;

import com.devthink.devthink_server.domain.Category;
import com.devthink.devthink_server.dto.CategoryDto;
import com.devthink.devthink_server.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 사용자의 HTTP 요청을 처리하는 클래스입니다.
 */

// 1. 카테고리 작성 -> POST /categories
// 2. 카테고리 검색 -> GET /categories/{id}
// 3. 글 업데이트 -> PUT /categories/{id}
// 4. 글 삭제 -> DELETE /categories/{id}

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 작성
     * @param categoryDto
     * @return
     */
    @PostMapping
    public Category save(@RequestBody CategoryDto categoryDto)
    {
        return categoryService.savePost(categoryDto);
    }

    /**
     * 카테고리의 id를 검색하여 게시글을 가져옵니다.
     * @param id 카테고리의 고유 id값
     * @return id의 카테고리
     */
    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id){
        Category data = categoryService.getCategory(id);
        return getCategoryData(data);

    }

    /**
     * 입력한 카테고리의 식별자 값과 valid한 카테고리의 정보를 받아, 기존의 게시글을 입력한 정보로 변경합니다.
     * @param id 카테고리의 식별자
     * @param categoryDto id, 이름
     * @return 기존 게시글의 정보 수정
     */
    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody @Valid CategoryDto categoryDto){
        Category update = categoryService.update(id, categoryDto);
        return getCategoryData(update);
    }

    /**
     * 입력한 카테고리의 식별자 값을 받아 게시글을 삭제합니다.
     * @param id 카테고리의 식별자
     */
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id)
    {
        categoryService.deletePost(id);
    }

    /**
     * 카테고리의 정보를 받아 게시글을 dto 데이터로 변환하여 반환합니다.
     * @param post 카테고리 정보
     * @return 입력된 dto 데이터로 변환된 값
     */

    private CategoryDto getCategoryData(Category category)
    {
        if(category == null)
            return null;

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
