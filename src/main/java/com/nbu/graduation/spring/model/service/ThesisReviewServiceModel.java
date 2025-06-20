package com.nbu.graduation.spring.model.service;


import java.time.LocalDate;

public class ThesisReviewServiceModel {
    private Long id;
    private String text;
    private LocalDate uploadDate;
    private boolean positive;

    private TeacherServiceModel reviewer;
    private ThesisServiceModel thesis;

    public ThesisReviewServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public TeacherServiceModel getReviewer() {
        return reviewer;
    }

    public void setReviewer(TeacherServiceModel reviewer) {
        this.reviewer = reviewer;
    }

    public ThesisServiceModel getThesis() {
        return thesis;
    }

    public void setThesis(ThesisServiceModel thesis) {
        this.thesis = thesis;
    }

    @Override
    public String toString() {
        return "ThesisReviewServiceModel{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", uploadDate=" + uploadDate +
                ", positive=" + positive +
                ", reviewer=" + reviewer +
                ", thesis=" + thesis +
                '}';
    }
}
