package com.example.bookrecommendation.repository;

import com.example.bookrecommendation.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);

    Boolean existsByUserIdAndBookId(Long userId, Long bookId);
}