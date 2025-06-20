package com.nbu.graduation.spring.service;

import com.nbu.graduation.spring.model.service.DefenseServiceModel;
import com.nbu.graduation.spring.model.service.ThesisReviewServiceModel;

import java.time.LocalDate;

public interface DefenseService {
    DefenseServiceModel createDefense(DefenseServiceModel defenseServiceModel);

    DefenseServiceModel updateDefense(DefenseServiceModel defenseServiceModel);

    void deleteDefenseById(Long id);

    DefenseServiceModel getDefenseById(Long id);

    void assignDefenseToStudent(ThesisReviewServiceModel thesisReviewServiceModel);

    void assignGradeToStudent(Long studentId, Double grade, Long teacherId);

    boolean setDefenseDateIfCommitteeMember(Long defenseId, Long teacherId, LocalDate defenseDate);

    DefenseServiceModel getDefenseByDate(LocalDate defenseDate);

    Double averagePresentedInPeriod(LocalDate from, LocalDate to);
}
