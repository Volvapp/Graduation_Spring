package com.nbu.graduation.spring.model.service;

import com.nbu.graduation.spring.model.enums.Position;



import java.util.List;

public class TeacherServiceModel {
    private Long id;
    private Position position;
    private boolean hasGraded;
    private List<ThesisAssignmentServiceModel> supervisedAssignments;
    private List<DefenseServiceModel> defenses;
    private List<ThesisReviewServiceModel> reviews;
    private UserServiceModel user;

    public TeacherServiceModel() {
        hasGraded = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isHasGraded() {
        return hasGraded;
    }

    public void setHasGraded(boolean hasGraded) {
        this.hasGraded = hasGraded;
    }

    public List<ThesisAssignmentServiceModel> getSupervisedAssignments() {
        return supervisedAssignments;
    }

    public void setSupervisedAssignments(List<ThesisAssignmentServiceModel> supervisedAssignments) {
        this.supervisedAssignments = supervisedAssignments;
    }

    public List<DefenseServiceModel> getDefenses() {
        return defenses;
    }

    public void setDefenses(List<DefenseServiceModel> defenses) {
        this.defenses = defenses;
    }

    public List<ThesisReviewServiceModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ThesisReviewServiceModel> reviews) {
        this.reviews = reviews;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TeacherServiceModel{" +
                "id=" + id +
                ", position=" + position +
                ", hasGraded=" + hasGraded +
                ", supervisedAssignments=" + supervisedAssignments +
                ", defenses=" + defenses +
                ", reviews=" + reviews +
                ", user=" + user +
                '}';
    }
}
