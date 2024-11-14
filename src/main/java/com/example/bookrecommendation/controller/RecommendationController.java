package com.example.bookrecommendation.controller;

import com.example.bookrecommendation.model.Book;
import com.example.bookrecommendation.security.UserPrincipal;
import com.example.bookrecommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<?> getRecommendations(@AuthenticationPrincipal UserPrincipal currentUser) {

        List<Book> recommendations = recommendationService.getRecommendations(currentUser.getId());

        return ResponseEntity.ok(recommendations);
    }
}