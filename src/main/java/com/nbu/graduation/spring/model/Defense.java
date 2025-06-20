package com.nbu.graduation.spring.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "defenses")
public class Defense extends BaseEntity {
    @Column(name = "defense_date")
    private LocalDate defenseDate;

    @ManyToMany
    private List<Teacher> committeeMembers;

    @OneToMany
    private List<Student> students;

    public Defense() {
        committeeMembers = new ArrayList<>();
        students = new ArrayList<>();
    }

    public LocalDate getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDate defenseDate) {
        this.defenseDate = defenseDate;
    }

    public List<Teacher> getCommitteeMembers() {
        return committeeMembers;
    }

    public void setCommitteeMembers(List<Teacher> committeeMembers) {
        this.committeeMembers = committeeMembers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}
