package com.example.bookrecommendation.service;

import com.example.bookrecommendation.model.*;
import com.example.bookrecommendation.repository.*;
import com.example.bookrecommendation.dto.ReviewRequest;
import com.example.bookrecommendation.security.UserPrincipal;
import com.example.bookrecommendation.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    public Review addReview(Long bookId, ReviewRequest reviewRequest, UserPrincipal currentUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("도서", "id", bookId));

        if (reviewRepository.existsByUserIdAndBookId(currentUser.getId(), bookId)) {
            throw new RuntimeException("이미 이 도서에 대한 리뷰를 작성하셨습니다.");
        }

        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        User user = new User();
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        review.setUser(user);
        review.setBook(book);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}