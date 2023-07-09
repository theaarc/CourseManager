package com.example.CourseManager.controllers;

import com.example.CourseManager.models.Course;
import com.example.CourseManager.models.Document;
import com.example.CourseManager.services.CourseService;
import com.example.CourseManager.services.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final DocumentService documentService;

    // Constructor injection
    public CourseController(CourseService courseService, DocumentService documentService) {
        this.courseService = courseService;
        this.documentService = documentService;
    }

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/courses/{id}")
    public String getCourseDetails(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        List<Document> documents = documentService.getDocumentsByCourse(course.getId());
        ArrayList<Document> docs = (ArrayList<Document>) documents;

        // Increment the views count
        course.setViews(course.getViews() + 1);
        courseService.updateCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("documents", documents);
        model.addAttribute("numberDocs",docs.toArray().length);

        return "course-details";
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute("course") Course course) {
        courseService.createCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "edit-course";
    }

    @PostMapping("/courses/{id}/edit")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("course") Course course) {
        course.setId(id);
        courseService.updateCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    @GetMapping("/courses/search")
    public String searchCourses(@RequestParam("keyword") String keyword, Model model) {
        List<Course> searchResults = courseService.searchCourses(keyword);
        model.addAttribute("courses", searchResults);
        return "redirect:/courses";
    }

}
