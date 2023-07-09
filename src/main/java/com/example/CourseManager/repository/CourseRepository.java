package com.example.CourseManager.repository;


import com.example.CourseManager.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingOrDescriptionContaining(String title, String description);
}

