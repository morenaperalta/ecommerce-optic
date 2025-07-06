package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.product.ProductResponseShort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category categoryEntity;
    private Category categoryEntityRepo;
    private CategoryResponse categoryResponse;
    private Long id;

    @BeforeEach
    void setUp() {
        id = 1L;
        categoryEntityRepo = new Category(1L, "Category 1");
        categoryEntity = new Category(1L, "Category 1");
        Product product = new Product(1L, "New product", 10.0, "image.url", true, List.of(categoryEntity));
        categoryEntity.addProducts(product);
        ProductResponseShort productResponseShort = new ProductResponseShort(1L, "New product", 10.0, true);
        categoryResponse = new CategoryResponse(1L, "Category 1", List.of(productResponseShort));
    }

    @Test
    void getAllCategories_whenCategoriesExist_returnsListOfCategoriesResponse() {
        when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));
        List<CategoryResponse> result = categoryService.getAllCategories();

        assertEquals(List.of(categoryResponse), result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_whenCategoryExists_returnsCategory() {
        when(categoryRepository.findById(eq(id))).thenReturn(Optional.of(categoryEntityRepo));
        Category result = categoryService.getCategoryById(id);

        assertEquals(categoryEntity, result);
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void getCategoryById_whenCategoryDoesNotExist_returnsException() {
        Exception expectedException = new EntityNotFoundException(Category.class.getSimpleName(), "id", id.toString());
        when(categoryRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryById(id));
        assertEquals(expectedException.getMessage(), resultException.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void getCategoryByName_whenCategoryExists_returnsCategory() {
        String name ="Category 1";
        when(categoryRepository.findCategoryByNameIgnoreCase(eq(name))).thenReturn(Optional.of(categoryEntity));
        Category result = categoryService.getCategoryByName(name);

        assertEquals(categoryEntityRepo, result);
        verify(categoryRepository, times(1)).findCategoryByNameIgnoreCase(name);
    }

    @Test
    void getCategoryByName_whenCategoryDoesNotExist_returnsException() {
        String name = "category 2";
        String expectedMessage = "Category with name " + name + " was not found";
        when(categoryRepository.findCategoryByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByName(name));
        assertEquals(expectedMessage, exception.getMessage());
        verify(categoryRepository, times(1)).findCategoryByNameIgnoreCase(name);
    }

    @Test
    void saveCategory_returnsCategoryResponse(){
        // Given
        CategoryRequest categoryRequest = new CategoryRequest("Category to Save");
        Category categoryEntityWithId = new Category(10L, "Category to Save", List.of());
        CategoryResponseShort categoryResponseShort = new CategoryResponseShort(10L, "Category to Save");

        // When
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryEntityWithId);
        CategoryResponseShort result = categoryService.saveCategory(categoryRequest);

        // Assert
        assertEquals(categoryResponseShort, result);
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(any(String.class));
        verify(categoryRepository, times(1)).save(any(Category.class));

    }

    @Test
    void saveCategoryExistingName_returnsCategoryResponse(){
        // Given
        CategoryRequest categoryRequest = new CategoryRequest("Category 1");
        Category categoryEntityWithoutId = new Category( "Category to Save");
        Category categoryEntityWithId = new Category(10L, "Category to Save", List.of());
        EntityAlreadyExistsException expectedException = new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name());

        // When
        when(categoryRepository.existsByNameIgnoreCase(categoryRequest.name())).thenThrow(new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name()));

        // Assert
        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> categoryService.saveCategory(categoryRequest));
        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(any(String.class));
    }
}