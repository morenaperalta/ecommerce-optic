package com.irisandco.ecommerce_optic.category;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService CATEGORY_SERVICE;

    public CategoryController(CategoryService categoryService) {
        CATEGORY_SERVICE = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponseList = CATEGORY_SERVICE.getAllCategories();
        return new ResponseEntity<>(categoryResponseList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CategoryResponseShort> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponseShort categoryResponseShort= CATEGORY_SERVICE.saveCategory(categoryRequest);
        return new ResponseEntity<>(categoryResponseShort, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseShort> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponseShort categoryResponseShort= CATEGORY_SERVICE.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(categoryResponseShort, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        CATEGORY_SERVICE.deleteCategory(id);
        return new ResponseEntity<>( "Category deleted successfully", HttpStatus.OK);
    }

}
