package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

//Activa Mockito en JUnit 5
@ExtendWith((MockitoExtension.class))
public class ProductServiceTest {

    //Creo un falso repositorio, que no se conecta a la base de datos.
    @Mock
    private ProductRepository productRepository;

    //Creo un servicio de categorías falso. Se usa cuando el ProductService necesita cargar categorías.
    @Mock
    private CategoryService categoryService;

    //Crea un ProductService de verdad, pero le inyecta los dos mocks anteriores.
    @InjectMocks
    private ProductService productService;

    private Product productEntity;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private Product productMapper;

    @BeforeEach
    void setUp() {
        productEntity = new Product(1L,"Sunglasses", 100.0, "image.jpg", true, List.of());
        productResponse = new ProductResponse(1L, "Sunglasses", 100.0, "image.jpg", true, List.of());
        productRequest = new ProductRequest("Sunglasses", 100.0, "image.jpg", true, List.of());
        productMapper =new Product("Sunglasses", 100.0, "image.jpg", true, List.of());
    }

    @Test
    void getAllProducts_whenProductExist_returnListOfProductResponse() {



    }


}
