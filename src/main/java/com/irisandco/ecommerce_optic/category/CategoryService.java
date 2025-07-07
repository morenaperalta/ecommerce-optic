package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository CATEGORY_REPOSITORY;

    public CategoryService(CategoryRepository categoryRepository) {
        CATEGORY_REPOSITORY = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories(){
        return listToDto(CATEGORY_REPOSITORY.findAll());
    }

    public Category getCategoryById(Long id){
        return CATEGORY_REPOSITORY.findById(id).orElseThrow(() -> new EntityNotFoundException(Category.class.getSimpleName(), "id", id.toString()));
    }

    public Category getCategoryByName(String name){
        return CATEGORY_REPOSITORY.findByNameIgnoreCase(name).orElseThrow(() -> new EntityNotFoundException(Category.class.getSimpleName(), "name", name));
    }

    public CategoryResponseShort saveCategory(CategoryRequest categoryRequest){
        if (CATEGORY_REPOSITORY.existsByNameIgnoreCase(categoryRequest.name())) {
            throw new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name());
        }
        Category category = CategoryMapper.toEntity(categoryRequest);
        return CategoryMapper.toDtoShort(CATEGORY_REPOSITORY.save(category));
    }

    public CategoryResponseShort updateCategory(Long id, CategoryRequest categoryRequest){
        Category category = getCategoryById(id);
        String newName = categoryRequest.name().trim();
        if(!category.getName().equals(newName)) {
            if (CATEGORY_REPOSITORY.existsByNameIgnoreCase(newName)) {
                throw new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name());
            }
        }
        category.setName(newName);
        return CategoryMapper.toDtoShort(CATEGORY_REPOSITORY.save(category));
    }

    public void deleteCategory(Long id){
        getCategoryById(id);
        CATEGORY_REPOSITORY.deleteById(id);
    }

    private List<CategoryResponse> listToDto(List<Category> categories) {
        return categories.stream()
                .map( CategoryMapper::toDto)
                .toList();
    }
}
