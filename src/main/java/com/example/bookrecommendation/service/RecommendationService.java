package com.example.bookrecommendation.service;

import com.example.bookrecommendation.model.*;
import com.example.bookrecommendation.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Cacheable(value = "recommendations", key = "#userId")
    public List<Book> getRecommendations(Long userId) {

        List<Review> allReviews = reviewRepository.findAll();

        Map<Long, Map<Long, Integer>> userRatings = new HashMap<>();
        for (Review review : allReviews) {
            Long uId = review.getUser().getId();
            Long bId = review.getBook().getId();
            Integer rating = review.getRating();

            userRatings.computeIfAbsent(uId, k -> new HashMap<>()).put(bId, rating);
        }

        Map<Long, Integer> targetUserRatings = userRatings.get(userId);
        if (targetUserRatings == null || targetUserRatings.isEmpty()) {
            return bookRepository.findAll().stream().limit(10).collect(Collectors.toList());
        }

        Map<Long, Double> similarityScores = new HashMap<>();
        for (Map.Entry<Long, Map<Long, Integer>> entry : userRatings.entrySet()) {
            Long otherUserId = entry.getKey();
            if (!otherUserId.equals(userId)) {
                Map<Long, Integer> otherUserRatings = entry.getValue();
                double similarity = calculateCosineSimilarity(targetUserRatings, otherUserRatings);
                similarityScores.put(otherUserId, similarity);
            }
        }

        List<Long> similarUsers = similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Map<Long, Double> recommendationScores = new HashMap<>();
        for (Long similarUserId : similarUsers) {
            Map<Long, Integer> similarUserRatings = userRatings.get(similarUserId);
            double similarity = similarityScores.get(similarUserId);

            for (Map.Entry<Long, Integer> entry : similarUserRatings.entrySet()) {
                Long bookId = entry.getKey();
                if (!targetUserRatings.containsKey(bookId)) {
                    double weightedScore = entry.getValue() * similarity;
                    recommendationScores.merge(bookId, weightedScore, Double::sum);
                }
            }
        }

        List<Long> recommendedBookIds = recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return bookRepository.findAllById(recommendedBookIds);
    }

    private double calculateCosineSimilarity(Map<Long, Integer> ratings1, Map<Long, Integer> ratings2) {
        Set<Long> commonBooks = new HashSet<>(ratings1.keySet());
        commonBooks.retainAll(ratings2.keySet());

        if (commonBooks.isEmpty()) {
            return 0.0;
        }

        double sumProduct = 0.0;
        double sumSquare1 = 0.0;
        double sumSquare2 = 0.0;

        for (Long bookId : commonBooks) {
            int rating1 = ratings1.get(bookId);
            int rating2 = ratings2.get(bookId);

            sumProduct += rating1 * rating2;
            sumSquare1 += rating1 * rating1;
            sumSquare2 += rating2 * rating2;
        }

        return sumProduct / (Math.sqrt(sumSquare1) * Math.sqrt(sumSquare2));
    }
}