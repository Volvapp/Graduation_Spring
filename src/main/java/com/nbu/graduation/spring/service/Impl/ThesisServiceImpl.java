package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.Thesis;
import com.nbu.graduation.spring.model.ThesisAssignment;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.repository.ThesisRepository;
import com.nbu.graduation.spring.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThesisServiceImpl implements ThesisService {
    private final ThesisRepository thesisRepository;
    private final ModelMapper modelMapper;
    private final ThesisAssignmentService assignmentService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserService userService;

    private final Path uploadDir = Paths.get("uploads/theses").toAbsolutePath().normalize();

    public ThesisServiceImpl(ThesisRepository thesisRepository, ModelMapper modelMapper, ThesisAssignmentService assignmentService,
                             StudentService studentService, TeacherService teacherService, UserService userService) {
        this.thesisRepository = thesisRepository;
        this.modelMapper = modelMapper;
        this.assignmentService = assignmentService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @Override
    public ThesisServiceModel createThesis(ThesisServiceModel thesisServiceModel, String filePath) {
        Thesis thesis = new Thesis();
        thesis.setTitle(thesisServiceModel.getTitle());
        thesis.setUploadDate(thesisServiceModel.getUploadDate());

        thesis.setAssignment(modelMapper.map(thesisServiceModel.getAssignment(), ThesisAssignment.class));

        thesis.setFilePath(filePath);

        Thesis saved = thesisRepository.save(thesis);
        return modelMapper.map(saved, ThesisServiceModel.class);
    }

    @Override
    public void uploadThesis(String title, MultipartFile file, String username) throws IOException {
        StudentServiceModel student = studentService.findByUsername(username);
        if (student == null || student.getAssignment() == null) {
            throw new RuntimeException("Student or assignment not found for username: " + username);
        }

        Files.createDirectories(uploadDir);

        String filename = username + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destinationPath = uploadDir.resolve(filename).normalize();

        if (!destinationPath.startsWith(uploadDir)) {
            throw new SecurityException("Invalid file path");
        }

        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        ThesisServiceModel thesis = new ThesisServiceModel();
        thesis.setTitle(title);
        thesis.setUploadDate(LocalDate.now());
        thesis.setAssignment(assignmentService.getAssignmentById(student.getAssignment().getId()));
        thesis.setFilePath(filename);

        this.createThesis(thesis, filename);
    }

    @Override
    public List<ThesisServiceModel> getThesesByDepartment(Department department) {
        List<Thesis> theses = thesisRepository.findByAssignment_Department(department);
        return theses.stream()
                .map(thesis -> modelMapper.map(thesis, ThesisServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean studentHasThesis(Long userId) {
        return thesisRepository.existsByAssignment_Student_User_Id(userId);
    }

    @Override
    public Resource loadThesisFile(String filename) throws MalformedURLException, FileNotFoundException {
        Path file = uploadDir.resolve(filename).normalize();

        if (!file.startsWith(uploadDir)) {
            throw new SecurityException("Invalid path");
        }

        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("File not found: " + filename);
        }
        return resource;
    }

    @Override
    public ThesisServiceModel updateThesis(ThesisServiceModel thesisServiceModel) {
        if (thesisServiceModel.getId() == null) {
            throw new RuntimeException("Thesis ID is required for update");
        }

        Optional<Thesis> existing = thesisRepository.findById(thesisServiceModel.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Thesis not found with ID: " + thesisServiceModel.getId());
        }

        Thesis thesisToUpdate = existing.get();
        thesisToUpdate.setTitle(thesisServiceModel.getTitle());
        thesisToUpdate.setUploadDate(thesisServiceModel.getUploadDate());
        thesisToUpdate.setFilePath(thesisServiceModel.getFilePath());
        thesisToUpdate.setAssignment(modelMapper.map(thesisServiceModel.getAssignment(), ThesisAssignment.class));

        Thesis updated = thesisRepository.save(thesisToUpdate);
        return modelMapper.map(updated, ThesisServiceModel.class);
    }

    @Override
    public void deleteThesisById(Long id) {
        Thesis thesis = thesisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thesis not found with ID: " + id));
        thesisRepository.delete(thesis);
    }

    @Override
    public ThesisServiceModel getThesisById(Long id) {
        Thesis thesis = thesisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thesis not found with ID: " + id));
        return modelMapper.map(thesis, ThesisServiceModel.class);
    }

    @Override
    public ThesisAssignmentServiceModel getStudentAssignment(String username) {
        StudentServiceModel student = studentService.findByUsername(username);
        if (student != null && student.getAssignment() != null) {
            return assignmentService.getAssignmentById(student.getAssignment().getId());
        }
        return null;
    }

    @Override
    public List<ThesisServiceModel> getThesesByTeacherDepartment(String username) {
        TeacherServiceModel teacher = teacherService.findByUsername(username);
        if (teacher == null) {
            return List.of();
        }
        Department dept = teacher.getUser().getDepartment();
        if (dept == null) {
            return List.of();
        }
        return getThesesByDepartment(dept);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    public ThesisServiceModel getThesisByStudentId(Long studentById) {
        Thesis thesis = thesisRepository.findByAssignment_Student_User_Id(studentById);
        if (thesis == null) {
            return null;
        }
        return modelMapper.map(thesis, ThesisServiceModel.class);
    }
}
