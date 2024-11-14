package com.example.bookrecommendation.controller;

import com.example.bookrecommendation.dto.ReviewRequest;
import com.example.bookrecommendation.model.Review;
import com.example.bookrecommendation.security.UserPrincipal;
import com.example.bookrecommendation.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequest reviewRequest,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {

        Review review = reviewService.addReview(bookId, reviewRequest, currentUser);

        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviewsByBookId(@PathVariable Long bookId) {

        List<Review> reviews = reviewService.getReviewsByBookId(bookId);

        return ResponseEntity.ok(reviews);
    }
}