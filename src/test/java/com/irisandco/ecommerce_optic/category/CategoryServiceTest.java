package com.irisandco.ecommerce_optic.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getAllCategories_returnsListOfCategoriesResponse() {
        // Mock de categoría
        Category categoryEntity = new Category("Category 1");

        when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));

        // Llamada al método real
        List<CategoryResponse> result = categoryService.getAllCategories();

        // Asserts básicos
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Category 1", result.get(0).name());
    }
}