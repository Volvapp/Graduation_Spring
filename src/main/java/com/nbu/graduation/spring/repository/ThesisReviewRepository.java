package com.nbu.graduation.spring.repository;

import com.nbu.graduation.spring.model.ThesisReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesisReviewRepository extends JpaRepository<ThesisReview, Long> {

    int countByPositive(boolean positive);
}
