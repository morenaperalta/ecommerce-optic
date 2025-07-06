package com.irisandco.ecommerce_optic.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irisandco.ecommerce_optic.category.CategoryResponseShort;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<ProductResponse> productResponses;

    @BeforeEach
    void setUp(){
        productResponses = new ArrayList<>();

        List<CategoryResponseShort> categories1 = List.of(
                new CategoryResponseShort(1L, "Category 1"),
                new CategoryResponseShort(2L, "Category 2")
        );
        List<CategoryResponseShort> categories2 = List.of(
                new CategoryResponseShort(3L, "Category 3"),
                new CategoryResponseShort(4L, "Category 4")
        );

        ProductResponse product1 = new ProductResponse(1L, "Product 1", 100.0, "image1.jpg", true, categories1);
        ProductResponse product2 = new ProductResponse(2L, "Product 2", 150.0, "image2.jpg", true, categories2);

        productResponses.add(product1);
        productResponses.add(product2);
    }

    @Test
    void shouldGetAllProductsSuccessfully() throws Exception {
        //Given
        given(productService.getAllProducts()).willReturn(productResponses);

        //When & Then
        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
                .andExpect(jsonPath("$[0].categories", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].categories[0].name").value("Category 1"));
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        //Given
        ProductRequest request = new ProductRequest("Product 3", 99.9, "image3.jpg", false, List.of("Category 1", "Category 2"));
        ProductResponse savedResponse = new ProductResponse(3L, "Product 3", 99.9, "image3.jpg", false, List.of(
                new CategoryResponseShort(1L, "Category 1"),
                new CategoryResponseShort(2L, "Category 2")));

        given(productService.createProduct(Mockito.any(ProductRequest.class))).willReturn(savedResponse);

        String json = objectMapper.writeValueAsString(request);

        //When & Then
        mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product 3"))
                .andExpect(jsonPath("$.categories", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.categories[0].name").value("Category 1"));

    }

    @Test
    void shouldReturnBadRequestWhenProductNameIsInvalid() throws Exception {
        //Given
        ProductRequest invalidRequest = new ProductRequest("", 0.0, "", false, List.of());
        String json = objectMapper.writeValueAsString(invalidRequest);

        //When & Then
        mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isBadRequest());
    }

}
