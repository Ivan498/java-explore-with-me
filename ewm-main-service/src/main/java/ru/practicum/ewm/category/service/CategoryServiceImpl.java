package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.util.Util;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        log.debug("Обработка запроса на добавление категории {}", category.toString());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        log.debug("Обработка запроса на обновление категории {}", category.toString());
        Category categoryInDb = getCategory(category.getId());
        Category categoryForUpdate = categoryInDb.toBuilder().name(category.getName()).build();
        return categoryRepository.save(categoryForUpdate);
    }

    @Override
    public void deleteCategory(Long id) {
        log.debug("Обработка запроса на удаление категории с id={}", id);
        // TODO: category link to events check
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(Category.class, id));
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories(Integer from, Integer size) {
        log.debug("Обработка запроса на просмотр категорий с {} по {}", from, size);
        return categoryRepository.findAllBy(Util.page(from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        log.debug("Обработка запроса на получение категории с id={}", id);
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(Category.class, id));
    }
}
