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
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;
    private Product product;
    private Long id;
    private String name;

    @BeforeEach
    void setUp() {
        String name = "Category ";
        id = 1L;
        categoryEntityRepo = new Category(1L, "Category 1");
        categoryEntity = new Category(1L, "Category 1");
        product = new Product(1L, "New product", 10.0,  "image.url", true, List.of(categoryEntity) );
        categoryEntity.addProducts(product);
        categoryRequest = new CategoryRequest("Category 2");
        ProductResponseShort productResponseShort = new ProductResponseShort(1L, "New product", 10.0, true);
        categoryResponse = new CategoryResponse(1L, "Category ", List.of(productResponseShort));
    }

    @Test
    void getAllCategories_whenCategoriesExist_returnsListOfCategoriesResponse() {

        when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));

        List<CategoryResponse> result = categoryService.getAllCategories();

//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        for (CategoryResponse item : result) {
//            assertInstanceOf(CategoryResponse.class, item);
//        }
//        assertEquals(1, result.size());
        assertEquals(List.of(categoryResponse), result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_whenCategoryExists_returnsCategory() {

        when(categoryRepository.findById(eq(id))).thenReturn(Optional.of(categoryEntityRepo));
        Category result = categoryService.getCategoryById(id);

//        assertNotNull(result);
        assertEquals(categoryEntity, result);
//        assertEquals("New product", result.getProducts().getFirst().getName());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void getCategoryById_whenCategoryDoesNotExist_returnsException() {
//        Exception expectedException = new EntityNotFoundException(Category.class.getSimpleName(), "id", id.toString());
        String expectedMessage = "Category with id " + id + " was not found";

        when(categoryRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryById(id));
//        assertEquals(expectedException, resultException);
        assertEquals(expectedMessage, resultException.getMessage());
        verify(categoryRepository, times(1)).findById(id);

    }

    @Test
    void getCategoryByNameExisting_returnsCategory() {

        when(categoryRepository.findCategoryByNameIgnoreCase(eq(name))).thenReturn(Optional.of(categoryEntity));

        Category result = categoryService.getCategoryByName(name);

        assertEquals(categoryEntity, result);
//        assertNotNull(result);
//        assertEquals(Category.class, result.getClass());
//        assertEquals("Category 1", result.getName());
//        assertEquals("New product", result.getProducts().getFirst().getName());

        verify(categoryRepository, times(1)).findCategoryByNameIgnoreCase(name);
    }

    @Test
    void getCategoryByNameNotExisting_returnsException() {
        String name = "category 2";
        String expectedMessage = "Category with name " + name + " was not found";

        when(categoryRepository.findCategoryByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByName(name));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void saveCategory_returnsCategoryResponse(){
        CategoryRequest categoryRequest = new CategoryRequest("Category to Save");

        Category categoryEntityWithId = new Category(10L, "Category to Save", List.of());

        when(categoryRepository.save(any(Category.class))).thenReturn(categoryEntityWithId);

        CategoryResponseShort result = categoryService.saveCategory(categoryRequest);

        System.out.println(categoryEntityWithId.getId());

        assertNotNull(result);
        assertEquals("Category to Save", result.name());
    }

    @Test
    void saveCategoryExistingName_returnsCategoryResponse(){
        CategoryRequest categoryRequest = new CategoryRequest("Category !");
//        CategoryResponse expectedResponse = new CategoryResponse(1L, "Category to Save", List.of());

        Category categoryEntity = new Category("Category to Save");
        Category categoryEntityWithId = new Category(10L, "Category to Save", List.of());

        Category category = CategoryMapper.toEntity(categoryRequest);

        when(categoryRepository.save(category)).thenReturn(categoryEntityWithId);

        CategoryResponseShort result = categoryService.saveCategory(categoryRequest);


        assertNotNull(result);
        assertEquals("Category to Save", result.name());


        String name = "category 1";
        String expectedMessage = "Category with name category 1 already exists";

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> categoryService.saveCategory(categoryRequest));
        assertEquals(expectedMessage, exception.getMessage());
    }
}