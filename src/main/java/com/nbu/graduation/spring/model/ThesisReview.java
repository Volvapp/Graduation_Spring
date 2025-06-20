package com.nbu.graduation.spring.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "thesis_reviews")
public class ThesisReview extends BaseEntity {
    @Column(name = "text")
    private String text;

    @Column(name = "upload_date")
    private LocalDate uploadDate;
    @Column(name = "positive")
    private boolean positive;

    @ManyToOne
    private Teacher reviewer;

    @OneToOne
    private Thesis thesis;

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

    public Teacher getReviewer() {
        return reviewer;
    }

    public void setReviewer(Teacher reviewer) {
        this.reviewer = reviewer;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }
}
