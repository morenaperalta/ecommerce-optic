package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.ProductMapper;
import com.irisandco.ecommerce_optic.product.ProductResponseShort;
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
        return CATEGORY_REPOSITORY.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public Category getCategoryByName(String name){
        return CATEGORY_REPOSITORY.findCategoryByName(name).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public CategoryResponseShort saveCategory(CategoryRequest categoryRequest){
        if (CATEGORY_REPOSITORY.existsByName(categoryRequest.name())) {
            new IllegalArgumentException("There is already a category named " + categoryRequest.name());
        }
        Category category = CategoryMapper.toEntity(categoryRequest);
        return CategoryMapper.toDtoShort(CATEGORY_REPOSITORY.save(category));
    }

    public CategoryResponseShort updateCategory(Long id, CategoryRequest categoryRequest){
        Category category = getCategoryById(id);
        if(!category.getName().equals(categoryRequest.name())) {
            if (CATEGORY_REPOSITORY.existsByName(categoryRequest.name())) {
                new IllegalArgumentException("There is already a category named " + categoryRequest.name());
            }
        }
        category.setName(categoryRequest.name());
        return CategoryMapper.toDtoShort(CATEGORY_REPOSITORY.save(category));
    }

    public void deleteCategory(Long id){
        Category category = getCategoryById(id);
        CATEGORY_REPOSITORY.deleteById(id);
    }

    private List<CategoryResponseShort> listToDtoShort(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDtoShort)
                .toList();
    }

    private List<CategoryResponse> listToDto(List<Category> categories) {
        return categories.stream()
                .map( (category) -> {
                                List <ProductResponseShort> productResponseShort = category.getProducts().stream()
                                                    .map(ProductMapper::toDtoShort)
                                                    .toList();
                                return CategoryMapper.toDto(category, productResponseShort);
                })
                .toList();
    }
}
