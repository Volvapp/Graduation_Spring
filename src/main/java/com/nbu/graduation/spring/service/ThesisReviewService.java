package com.nbu.graduation.spring.service;

import com.nbu.graduation.spring.model.service.ThesisReviewServiceModel;

public interface ThesisReviewService {
    ThesisReviewServiceModel createThesisReview(ThesisReviewServiceModel thesisReviewServiceModel);

    ThesisReviewServiceModel getThesisReviewById(Long thesisReviewId);

    ThesisReviewServiceModel updateThesisReview(ThesisReviewServiceModel thesisReviewServiceModel);

    void deleteThesisReview(Long thesisReviewId);

    ThesisReviewServiceModel prepareReviewForm(Long thesisId, String username);

    void submitReview(String text, boolean positive, Long thesisId, String username);

    long countNegativeReviews();
}
