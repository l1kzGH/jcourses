package com.likz.spring.course.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findAllById(Long id);
}
