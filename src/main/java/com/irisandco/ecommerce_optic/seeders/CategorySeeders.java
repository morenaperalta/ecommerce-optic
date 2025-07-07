package com.irisandco.ecommerce_optic.seeders;

import com.irisandco.ecommerce_optic.category.Category;
import com.irisandco.ecommerce_optic.category.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
public class CategorySeeders implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> categoryNames = List.of(
                "Eyewear",
                "Sunglasses",
                "Contact Lenses",
                "Accessories",
                "Eyeglasses",
                "Polarized",
                "Solution",
                "Cleaning"
        );

        for (String name : categoryNames) {
            if (!categoryRepository.existsByNameIgnoreCase(name)) {
                categoryRepository.save(new Category(name));
            }
        }
    }
}
