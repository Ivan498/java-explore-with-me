package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category addCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long id);

    List<Category> getCategories(Integer from, Integer size);

    Category getCategory(Long id);

}
