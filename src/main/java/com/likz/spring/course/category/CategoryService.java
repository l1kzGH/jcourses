package com.likz.spring.course.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public List<CategoryResponse> findAll() {
        List<Category> categories = new ArrayList<>(categoryRepo.findAll());
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category c : categories) {
            categoryResponses.add(new CategoryResponse(
                    c.getName()
            ));
        }

        return categoryResponses;
    }

    public Category findById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public Category findByName(String name) {
        return categoryRepo.findByName(name).orElse(null);
    }

    public ResponseEntity<String> create(Category category) {
        categoryRepo.save(category);
        return ResponseEntity.ok("Category created");
    }

}
