package com.irisandco.ecommerce_optic.category;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.ErrorResponse;
import com.irisandco.ecommerce_optic.exception.GlobalExceptionHandler;
import com.irisandco.ecommerce_optic.product.ProductResponseShort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private CategoryResponseShort categoryResponseShort;
    private String jsonCategory1Response;
    private String jsonCategoryRequest;
    private Category category1;
    private CategoryResponse categoryResponse1;
    private CategoryResponse categoryResponse2;
    private CategoryRequest categoryRequest;
    private Long id;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        id = 1L;
        category1 = new Category(1L, "Category 1", List.of());
        categoryRequest = new CategoryRequest("Category 1");
        categoryResponseShort = new CategoryResponseShort(1L, "Category 1");

        ProductResponseShort productResponseShort = new ProductResponseShort(1L, "New product", 10.0, true);
        categoryResponse1 = new CategoryResponse(1L, "Category 1", List.of(productResponseShort));
        categoryResponse2 = new CategoryResponse(2L, "Category 2", List.of(productResponseShort));


        jsonCategory1Response = objectMapper.writeValueAsString(categoryResponseShort);
        jsonCategoryRequest = objectMapper.writeValueAsString(categoryRequest);


    }

    @Test
    void getAllCategories_whenCategoriesExist_returnsListOfCategoriesResponse() throws Exception {
        // Given
        ProductResponseShort productResponseShort = new ProductResponseShort(1L, "New product", 10.0, true);
        String expectedJson = objectMapper.writeValueAsString(List.of(
                new CategoryResponse(1L, "Category 1", List.of(productResponseShort)),
                new CategoryResponse(2L, "Category 2", List.of(productResponseShort))));
        List<CategoryResponse> categoryResponses = List.of(categoryResponse1, categoryResponse2);
        given(categoryService.getAllCategories()).willReturn(categoryResponses);

        // When & Then
        mockMvc.perform(get("/api/categories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void createCategory_whenCategoryIsNew_returnsCategory() throws Exception{
        // Given
        String expectedJson = objectMapper.writeValueAsString(new CategoryResponseShort(1L, "Category 1"));
        given(categoryService.saveCategory(categoryRequest)).willReturn(categoryResponseShort);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCategoryRequest))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));

    }

//    @Test
//    void createCategory_whenCategoryIsRepeated_returnsException() throws Exception{
//        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
//        EntityAlreadyExistsException exception = new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name());
////        ResponseEntity<ErrorResponse>  responseEntity = globalExceptionHandler.handleEntityAlreadyExistsException(exception, mockHttpServletRequest);
//
////        HttpStatus status = HttpStatus.NOT_FOUND;
////        ErrorResponse errorResponse = new ErrorResponse(status, exception.getMessage(), controllerPath);
////        ResponseEntity responseEntity = new ResponseEntity<>(errorResponse,status);
//
//        given(categoryService.saveCategory(categoryRequest)).willThrow(exception);
//        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage(), "http://localhost/api/categories");
//        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
//
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonCategoryRequest))
//                .andExpect(status().isConflict())
//                .andExpect(result -> assertEquals(result.getResolvedException(), exception))
//                .andExpect(content().json(jsonErrorResponse));
////                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
////                .andExpect(jsonPath("$.path").value("http://localhost/api/categories"));
//
//    }

    @Test
    void createCategory_whenCategoryIsRepeated_returnsConflict() throws Exception {
        // Given
        String jsonCategoryRequest = objectMapper.writeValueAsString(categoryRequest);
        EntityAlreadyExistsException exception = new EntityAlreadyExistsException(Category.class.getSimpleName(), "name", categoryRequest.name());
        String expectedJson = objectMapper.writeValueAsString(new ErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                "http://localhost/api/categories"
        ));
        given(categoryService.saveCategory(any(CategoryRequest.class))).willThrow(exception);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCategoryRequest))
                .andExpect(status().isConflict())
                .andExpect(result -> assertEquals(exception, result.getResolvedException()))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void createCategory_whenCategoryNameIsInvalid_returnsException() throws Exception {
        // Given
        CategoryRequest invalidRequest = new CategoryRequest("C");

        String jsonRequest= objectMapper.writeValueAsString(invalidRequest);

        // Create a BindingResult with validation errors
//        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(invalidRequest, "invalidRequest");
//        bindingResult.addError(new FieldError("categoryRequest", "name", "Category name must contain min 2 and max 50 characters"));
//        MethodParameter mockParameter = new MethodParameter(
//                CategoryController.class.getMethod("createCategory", CategoryRequest.class), 0
//        );
//        // Simulate the exception
//        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(mockParameter, bindingResult);

        String message = "Category name must contain min 2 and max 50 characters";
        String expectedJson = new ObjectMapper().writeValueAsString(
                new HashMap<String, String>() {{
                    put("name", message);
                }}
        );

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(MethodArgumentNotValidException.class, result.getResolvedException().getClass()))
                .andExpect(content().json(expectedJson));
    }


//
//    @Test
//    void createCategory_whenCategoryNameIsInvalid_returnsException() throws Exception {
//        // Given
//        CategoryRequest invalidRequest = new CategoryRequest("");
//        String json = objectMapper.writeValueAsString(invalidRequest);
//
//        String string = "Category name must contain min 2 and max 50 characters";
////        String jsonResponse = objectMapper.writeValueAsString(string);
////        JSONObject jsonObject = new JSONObject(string);
//
//        String message = "Category name must contain min 2 and max 50 characters";
//
//        String jsonResponse = new ObjectMapper().writeValueAsString(
//                new JSONObject().put("message", message)
//        );
//        // When & Then
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(jsonResponse));
//    }

}