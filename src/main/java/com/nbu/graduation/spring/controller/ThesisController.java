package com.nbu.graduation.spring.controller;

import com.nbu.graduation.spring.model.dto.ThesisDTO;
import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/theses")
public class ThesisController {
    private final ThesisService thesisService;

    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ThesisReviewService thesisReviewService;

    public ThesisController(ThesisService thesisService, TeacherService teacherService, StudentService studentService, ThesisReviewService thesisReviewService) {
        this.thesisService = thesisService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.thesisReviewService = thesisReviewService;
    }


    @GetMapping
    public String showThesisPage(Model model, Principal principal) {
        String username = principal.getName();
        UserServiceModel user = thesisService.findUserByUsername(username);

        if (user.getRole().equals(Role.STUDENT)) {
            boolean hasThesis = thesisService.studentHasThesis(user.getId());
            if (hasThesis) {
                ThesisServiceModel thesis = thesisService.getThesisByStudentId(this.studentService.getStudentById(user.getId()).getId());
                model.addAttribute("thesis", thesis);
            }
            ThesisAssignmentServiceModel assignment = thesisService.getStudentAssignment(username);


            model.addAttribute("hasThesis", hasThesis);
            model.addAttribute("assignment", assignment);
            model.addAttribute("isStudent", true);

        } else if (user.getRole().equals(Role.TEACHER)) {
            List<ThesisServiceModel> thesesFromDepartment = thesisService.getThesesByTeacherDepartment(username);
            TeacherServiceModel teacher = teacherService.findByUsername(username);

            model.addAttribute("isTeacher", true);
            model.addAttribute("teacher", teacher);
            model.addAttribute("thesesFromDepartment", thesesFromDepartment);
        }

        model.addAttribute("user", user);
        return "theses";
    }


    @PostMapping("/upload")
    public String uploadThesis(@RequestParam("title") String title,
                               @RequestParam("file") MultipartFile file,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        try {
            thesisService.uploadThesis(title, file, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Дипломната работа беше успешно качена.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Възникна грешка при качване на файла.");
            e.printStackTrace();
        }
        return "redirect:/theses";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadThesis(@RequestParam("path") String filePath) {
        try {
            Resource resource = thesisService.loadThesisFile(filePath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reviews")
    public String showReviewForm(@RequestParam("thesisId") Long thesisId,
                                 Principal principal,
                                 Model model) {

        ThesisReviewServiceModel review = thesisReviewService.prepareReviewForm(thesisId, principal.getName());
        ThesisServiceModel thesis = review.getThesis();

        model.addAttribute("review", review);
        model.addAttribute("thesis", thesis);
        return "theses-reviews";
    }

    @PostMapping("/reviews")
    public String submitReview(@RequestParam("text") String text,
                               @RequestParam("positive") boolean positive,
                               @RequestParam("thesisId") Long thesisId,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {

        thesisReviewService.submitReview(text, positive, thesisId, principal.getName());

        redirectAttributes.addFlashAttribute("success", "Рецензията е записана успешно.");
        return "redirect:/theses";
    }
}