package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.Defense;
import com.nbu.graduation.spring.service.DefenseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DefenseRepository extends JpaRepository<Defense, Long> {
    Defense findByDefenseDate(LocalDate defenseDate);

    List<Defense> findAllByDefenseDateBetween(LocalDate from, LocalDate to);
}
