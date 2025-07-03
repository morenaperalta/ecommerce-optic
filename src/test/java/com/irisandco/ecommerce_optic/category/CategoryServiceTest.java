package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    private Category categoryEntity;

    private Product product;

    Long id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
        id = 1L;
        product = new Product();
        product.setName("New product");
        categoryEntity = new Category(id, "Category 1", List.of(product));
    }

    @Test
    void getAllCategories_returnsListOfCategoriesResponse() {
        // Mock de categoría


        when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));

        // Llamada al método real
        List<CategoryResponse> result = categoryService.getAllCategories();

        // Asserts básicos
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Category 1", result.get(0).name());
    }

    @Test
    void getCategoryByIdExisting_returnsCategory() {

        when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryEntity));

        // Llamada al método real
        Category result = categoryService.getCategoryById(id);

        // Asserts básicos
        assertNotNull(result);
        assertEquals("Category 1", result.getName());
        assertEquals("New product", result.getProducts().get(0).getName());
    }
}