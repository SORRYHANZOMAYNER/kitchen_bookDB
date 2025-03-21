package org.example.services;

import org.example.models.Feedback;
import org.example.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Feedback findById(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }
    @Transactional
    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
    @Transactional
    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }
}
