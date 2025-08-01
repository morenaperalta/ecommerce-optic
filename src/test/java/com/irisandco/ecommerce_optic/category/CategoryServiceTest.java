package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.product.ProductResponseShort;
import org.junit.jupiter.api.AfterEach;
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
    
    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(categoryRepository);
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
        when(categoryRepository.findByNameIgnoreCase(eq(name))).thenReturn(Optional.of(categoryEntity));
        Category result = categoryService.getCategoryByName(name);

        assertEquals(categoryEntityRepo, result);
        verify(categoryRepository, times(1)).findByNameIgnoreCase(name);
    }

    @Test
    void getCategoryByName_whenCategoryDoesNotExist_returnsException() {
        String name = "category 2";
        String expectedMessage = "Category with name " + name + " was not found";
        when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByName(name));
        assertEquals(expectedMessage, exception.getMessage());
        verify(categoryRepository, times(1)).findByNameIgnoreCase(name);
    }

    @Test
    void saveCategory_whenCategoryIsNew_returnsCategoryResponse(){
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
    void saveCategory_whenCategoryAlreadyExists_returnsCategoryResponse(){
        // Given
        CategoryRequest categoryRequest = new CategoryRequest("Category 1");
        EntityAlreadyExistsException expectedException = new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name());

        // When
        when(categoryRepository.existsByNameIgnoreCase(categoryRequest.name())).thenThrow(new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name()));

        // Assert
        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> categoryService.saveCategory(categoryRequest));
        assertEquals(expectedException.getMessage(), exception.getMessage());
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(any(String.class));
    }

    @Test
    void updateCategory_whenCategoryExists_returnsCategoryResponse(){
        // Given
        Long id = 10L;
        CategoryRequest categoryRequest = new CategoryRequest("Category updated");
        Category categoryEntityWithId = new Category(10L, "Category", List.of());
        Category categoryEntityWithIdUpdated = new Category(10L, "Category updated", List.of());
        CategoryResponseShort categoryResponseShort = new CategoryResponseShort(10L, "Category updated");

        // When
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryEntityWithIdUpdated);
        when(categoryRepository.findById(eq(id))).thenReturn(Optional.of(categoryEntityWithId));
        CategoryResponseShort result = categoryService.updateCategory(id, categoryRequest);

        // Assert
        assertEquals(categoryResponseShort, result);
        verify(categoryRepository, times(1)).existsByNameIgnoreCase(any(String.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_whenCategoryDoesNotExist_returnsException() {
        // Given
        Long id = 10L;
        EntityNotFoundException expectedExceptions = new EntityNotFoundException(Category.class.getSimpleName(), "id", id.toString());
        CategoryRequest categoryRequest = new CategoryRequest("Category updated");

        // When
        when(categoryRepository.findById(eq(id))).thenThrow(expectedExceptions);

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {categoryService.updateCategory(id, categoryRequest);});

        // Assert
        assertEquals(expectedExceptions.getMessage(), exception.getMessage());
        verify(categoryRepository, times(1)).findById(eq(id));
    }

    @Test
    void deleteCategory_whenCategoryExists_returnsVoid() {
        when(categoryRepository.findById(eq(id))).thenReturn(Optional.of(categoryEntityRepo));
        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteCategory(id);
        
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCategory_whenCategoryDoesNotExist_returnsException() {
        Long id = 20L;
        Exception expectedException = new EntityNotFoundException(Category.class.getSimpleName(), "id", id.toString());
        when(categoryRepository.findById(eq(id))).thenThrow(new EntityNotFoundException(Category.class.getSimpleName(), "id", id.toString()));

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(id));
        assertEquals(expectedException.getMessage(), resultException.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }
}