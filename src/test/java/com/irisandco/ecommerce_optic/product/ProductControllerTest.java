package com.irisandco.ecommerce_optic.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@WebMvcTest(ProductControllerTest.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ProductService productServiceMock;

    @BeforeEach
    //Antes de cada test prepara esto (setup)
    public void setUp() {
        //Creo un objeto falso que imita el servicio. No ejecuta lógica real.
        productServiceMock = Mockito.mock(ProductService.class);

        //Creo controlador real manualmente, pero le doy el servicio falso con el mock.
        ProductController productController = new ProductController(productServiceMock);

        //Creo manualmente el entorno de test para poder simular peticiones HTTP
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    /*@Test
    void getAllProducts_returnList() throws Exception {
        ProductResponse product1 = new ProductResponse("Product 1", List.of("subproducts"));

        Mockito.when(ProductService.getAllProducts()).thenReturn(List.of(product1));
    }*/
}
