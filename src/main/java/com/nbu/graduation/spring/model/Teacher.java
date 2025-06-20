package com.nbu.graduation.spring.model;

import com.nbu.graduation.spring.model.enums.Position;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "teachers")
public class Teacher extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "has_graded")
    private boolean hasGraded;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.EAGER)
    private List<ThesisAssignment> supervisedAssignments;

    @ManyToMany(mappedBy = "committeeMembers", fetch = FetchType.EAGER)
    private List<Defense> defenses;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.EAGER)
    private List<ThesisReview> reviews;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Teacher() {
        this.position = Position.ASSISTANT;
        this.hasGraded = false;
        this.supervisedAssignments = new ArrayList<>();
        this.defenses = new ArrayList<>();
        this.reviews = new ArrayList<>();
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

    public List<ThesisAssignment> getSupervisedAssignments() {
        return supervisedAssignments;
    }

    public void setSupervisedAssignments(List<ThesisAssignment> supervisedAssignments) {
        this.supervisedAssignments = supervisedAssignments;
    }

    public List<Defense> getDefenses() {
        return defenses;
    }

    public void setDefenses(List<Defense> defenses) {
        this.defenses = defenses;
    }

    public List<ThesisReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ThesisReview> reviews) {
        this.reviews = reviews;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
