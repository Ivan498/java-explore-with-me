package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос на добавление категории {}", newCategoryDto.toString());
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryService.addCategory(category));
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId,
                                     @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос на обновление категории с id={}", catId);
        Category category = CategoryMapper.toCategory(categoryDto);
        Category categoryWithId = category.toBuilder().id(catId).build();
        return CategoryMapper.toCategoryDto(categoryService.updateCategory(categoryWithId));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Получен запрос на удаление категории с id={}", catId);
        categoryService.deleteCategory(catId);
    }
}
