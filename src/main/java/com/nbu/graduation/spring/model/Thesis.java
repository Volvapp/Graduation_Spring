package com.nbu.graduation.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "theses")
public class Thesis extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @OneToOne
    private ThesisAssignment assignment;

    @OneToOne(mappedBy = "thesis")
    private ThesisReview review;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ThesisAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(ThesisAssignment assignment) {
        this.assignment = assignment;
    }

    public ThesisReview getReview() {
        return review;
    }

    public void setReview(ThesisReview review) {
        this.review = review;
    }
}
