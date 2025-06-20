package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.ThesisReview;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.repository.ThesisReviewRepository;
import com.nbu.graduation.spring.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ThesisReviewServiceImpl implements ThesisReviewService {
    private final ThesisReviewRepository thesisReviewRepository;
    private final ModelMapper modelMapper;
    private final ThesisService thesisService;
    private final TeacherService teacherService;
    private final UserService userService;
    private final DefenseService defenseService;

    public ThesisReviewServiceImpl(ThesisReviewRepository thesisReviewRepository,
                                   ModelMapper modelMapper, @Lazy ThesisService thesisService,
                                   TeacherService teacherService, UserService userService, DefenseService defenseService) {
        this.thesisReviewRepository = thesisReviewRepository;
        this.modelMapper = modelMapper;
        this.thesisService = thesisService;
        this.teacherService = teacherService;
        this.userService = userService;
        this.defenseService = defenseService;
    }

    @Override
    public ThesisReviewServiceModel createThesisReview(ThesisReviewServiceModel thesisReviewServiceModel) {
        ThesisReview saved = thesisReviewRepository.save(modelMapper.map(thesisReviewServiceModel, ThesisReview.class));
        return modelMapper.map(saved, ThesisReviewServiceModel.class);
    }

    @Override
    public ThesisReviewServiceModel getThesisReviewById(Long thesisReviewId) {
        ThesisReview thesisReview = thesisReviewRepository.findById(thesisReviewId)
                .orElseThrow(() -> new RuntimeException("Thesis review with ID " + thesisReviewId + " not found."));
        return modelMapper.map(thesisReview, ThesisReviewServiceModel.class);
    }

    @Override
    public ThesisReviewServiceModel updateThesisReview(ThesisReviewServiceModel thesisReviewServiceModel) {
        if (thesisReviewServiceModel.getId() == null ||
                !thesisReviewRepository.existsById(thesisReviewServiceModel.getId())) {
            throw new RuntimeException("Thesis review to update not found.");
        }
        ThesisReview saved = thesisReviewRepository.save(modelMapper.map(thesisReviewServiceModel, ThesisReview.class));
        return modelMapper.map(saved, ThesisReviewServiceModel.class);
    }

    @Override
    public void deleteThesisReview(Long thesisReviewId) {
        ThesisReview thesisReview = thesisReviewRepository.findById(thesisReviewId)
                .orElseThrow(() -> new RuntimeException("Thesis review with ID " + thesisReviewId + " not found."));
        thesisReviewRepository.delete(thesisReview);
    }

    @Override
    public ThesisReviewServiceModel prepareReviewForm(Long thesisId, String username) {
        UserServiceModel user = userService.findByUsername(username);
        TeacherServiceModel teacher = teacherService.findByUserId(user.getId());
        ThesisServiceModel thesis = thesisService.getThesisById(thesisId);

        if (teacher == null) {
            throw new RuntimeException("Teacher for user " + username + " not found.");
        }
        if (thesis == null) {
            throw new RuntimeException("Thesis with ID " + thesisId + " not found.");
        }

        ThesisReviewServiceModel review = new ThesisReviewServiceModel();
        review.setReviewer(teacher);
        review.setThesis(thesis);

        return review;
    }

    @Override
    public void submitReview(String text, boolean positive, Long thesisId, String username) {
        ThesisReviewServiceModel review = buildReview(text, positive, thesisId, username);

        createThesisReview(review);

        if (positive) {
            this.defenseService.assignDefenseToStudent(review);
        }
    }

    @Override
    public long countNegativeReviews() {
        return this.thesisReviewRepository.countByPositive(false);
    }

    private ThesisReviewServiceModel buildReview(String text, boolean positive, Long thesisId, String username) {
        UserServiceModel user = userService.findByUsername(username);
        TeacherServiceModel teacher = teacherService.findByUserId(user.getId());
        ThesisServiceModel thesis = thesisService.getThesisById(thesisId);

        if (teacher == null) {
            throw new RuntimeException("Teacher for user " + username + " not found.");
        }
        if (thesis == null) {
            throw new RuntimeException("Thesis with ID " + thesisId + " not found.");
        }

        ThesisReviewServiceModel review = new ThesisReviewServiceModel();
        review.setText(text);
        review.setPositive(positive);
        review.setReviewer(teacher);
        review.setThesis(thesis);
        review.setUploadDate(LocalDate.now());

        return review;
    }
}
