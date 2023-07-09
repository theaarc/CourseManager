package com.example.CourseManager.controllers;

import com.example.CourseManager.models.Course;
import com.example.CourseManager.models.Document;
import com.example.CourseManager.services.CourseService;
import com.example.CourseManager.services.DocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final CourseService courseService;
    @Value("${upload.dir}")
    private String uploadDir;

    public DocumentController(DocumentService documentService, CourseService courseService) {
        this.documentService = documentService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public String getDocumentDetails(@PathVariable Long id, Model model){
        Document document = documentService.getDocumentById(id);
        model.addAttribute("document", document);
        return "document-details";
    }
    @GetMapping("/course/{courseId}")
    public String getDocumentsByCourse(@PathVariable Long courseId, Model model) {
        List<Document> documents = documentService.getDocumentsByCourse(courseId);
        model.addAttribute("documents", documents);
        return "document-list";
    }

    @GetMapping("/create/{courseId}")
    public String showCreateForm(@PathVariable Long courseId, Model model) {
        Course course = courseService.getCourseById(courseId);
        model.addAttribute("course", course);
        model.addAttribute("document", new Document());
        return "document-create";
    }

    @PostMapping("/create/{courseId}")
    public String createDocument(@ModelAttribute("document") Document document, @RequestParam("file") MultipartFile file, @PathVariable Long courseId, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            // Handle the case when no file is selected
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file.");
            return "redirect:/documents/course/" + document.getCourseId();
        }

        try {
            // Create the uploads directory if it doesn't exist
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // Generate a unique file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

            // Move the uploaded file to the uploads directory
            Path filePath = Paths.get(uploadPath.getAbsolutePath(), uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            // Set the file path in the document object
            document.setFilePath(String.valueOf(filePath));
            document.setCourseId(courseId);

            // Save the document to the database
            documentService.createDocument(document);
        } catch (IOException e) {
            // Handle any exceptions that occur during file upload
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload the file.");
        }

        return "redirect:/courses/" + document.getCourseId();
    }


    @GetMapping("/edit/{documentId}")
    public String showEditForm(@PathVariable Long documentId, Model model) {
        Document document = documentService.getDocumentById(documentId);
        model.addAttribute("document", document);
        return "document-edit";
    }

    @PostMapping("/edit{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute("document") Document document, @RequestParam("file") MultipartFile file) {
        // Retrieve the existing document from the database
        Document existingDocument = documentService.getDocumentById(document.getId());

        // Check if a new file was uploaded
        if (!file.isEmpty()) {
            // Delete the old document file
            deleteDocumentFile(existingDocument);

            // Save the new document file to the upload directory
            String newFilePath = saveDocumentFile(file);

            // Update the document's path in the database
            existingDocument.setFilePath(newFilePath);
        }

        // Update other fields of the document
        existingDocument.setTitle(document.getTitle());

        // Save the updated document in the database
        document.setId(id);
        documentService.updateDocument(existingDocument);

        return "redirect:/courses/" + document.getCourseId();
    }

    @PostMapping("/delete/{documentId}")
    public String deleteDocument(@PathVariable Long documentId) {
        Document document = documentService.getDocumentById(documentId);
        deleteDocumentFile(document);
        Long courseId = document.getCourseId();
        documentService.deleteDocument(documentId);
        return "redirect:/courses/" + courseId;
    }

    private void deleteDocumentFile(Document document) {
        // Get the file path of the document
        String filePath = document.getFilePath();

        // Delete the file from the upload directory
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private String saveDocumentFile(MultipartFile file) {
        try {
            // Create the uploads directory if it doesn't exist
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // Generate a unique file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

            // Move the uploaded file to the uploads directory
            Path filePath = Paths.get(uploadPath.getAbsolutePath(), uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            return String.valueOf(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
}

