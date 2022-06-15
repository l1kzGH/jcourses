package com.likz.spring.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CourseRepo extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

}
