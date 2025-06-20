package com.nbu.graduation.spring.model.service;

import java.time.LocalDate;

public class ThesisServiceModel {
    private Long id;
    private String title;
    private String filePath;
    private LocalDate uploadDate;
    private ThesisAssignmentServiceModel assignment;
    private ThesisReviewServiceModel review;

    public ThesisServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ThesisAssignmentServiceModel getAssignment() {
        return assignment;
    }

    public void setAssignment(ThesisAssignmentServiceModel assignment) {
        this.assignment = assignment;
    }

    public ThesisReviewServiceModel getReview() {
        return review;
    }

    public void setReview(ThesisReviewServiceModel review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "ThesisServiceModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", filePath='" + filePath + '\'' +
                ", uploadDate=" + uploadDate +
                ", assignment=" + assignment +
                ", review=" + review +
                '}';
    }
}
