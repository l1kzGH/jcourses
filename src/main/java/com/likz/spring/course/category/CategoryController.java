package com.likz.spring.course.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) return ResponseEntity.status(404).body("Not found");

        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Category category) {
        Category category1 = categoryService.findByName(category.getName());
        if (category1 != null) return ResponseEntity.status(404).body("This category already exists");


        return ResponseEntity.ok(categoryService.create(category));
    }

}
