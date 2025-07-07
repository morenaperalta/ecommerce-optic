package com.irisandco.ecommerce_optic.product;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService PRODUCT_SERVICE;

    public ProductController(ProductService productService) {
        PRODUCT_SERVICE = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getAllProducts() { List<ProductResponse> products = PRODUCT_SERVICE.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse gotProduct = PRODUCT_SERVICE.getProductResponseById(id);
        return new ResponseEntity<>(gotProduct, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) { ProductResponse createdProduct = PRODUCT_SERVICE.createProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = PRODUCT_SERVICE.updateProduct(id, productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        PRODUCT_SERVICE.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponse>> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ProductResponse> products = PRODUCT_SERVICE.filterProducts(name, categoryName, minPrice, maxPrice);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
