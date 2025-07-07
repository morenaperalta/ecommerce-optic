package com.irisandco.ecommerce_optic.seeders;

import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.product.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import java.util.List;

import com.irisandco.ecommerce_optic.category.Category;
import com.irisandco.ecommerce_optic.category.CategoryRepository;
import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductSeeders implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            // Predefine category names used in products
            Map<String, Category> categoryMap = new HashMap<>();

            List<String> categoryNames = List.of(
                    "Eyewear",
                    "Sunglasses",
                    "Contact Lenses",
                    "Accessories",
                    "Classic",
                    "Polarized",
                    "Solution",
                    "Cleaning"
            );

            // Fetch existing categories or create them if they don't exist
            for (String categoryName : categoryNames) {
                Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                        .orElseGet(() -> {
                            Category newCategory = new Category(categoryName);
                            categoryRepository.save(newCategory);
                            return newCategory;
                        });
                categoryMap.put(categoryName, category);
            }

            // Create products with associated categories
            List<Product> products = List.of(
                    new Product(
                            "Classic Eyeglasses",
                            59.99,
                            "images/eyeglasses_classic.png",
                            false,
                            List.of(
                                    categoryMap.get("Eyewear"),
                                    categoryMap.get("Classic")
                            )
                    ),
                    new Product(
                            "Polarized Sunglasses",
                            89.99,
                            "images/sunglasses_polarized.png",
                            false,
                            List.of(
                                    categoryMap.get("Sunglasses"),
                                    categoryMap.get("Polarized")
                            )
                    ),
                    new Product(
                            "Contact Lens Solution",
                            12.99,
                            "images/contact_solution.png",
                            true,
                            List.of(
                                    categoryMap.get("Contact Lenses"),
                                    categoryMap.get("Solution")
                            )
                    ),
                    new Product(
                            "Eyeglass Cleaning Cloth",
                            3.99,
                            "images/cleaning_cloth.png",
                            false,
                            List.of(
                                    categoryMap.get("Accessories"),
                                    categoryMap.get("Cleaning")
                            )
                    )
            );

            productRepository.saveAll(products);
        }
    }
}

