package com.poly.service;

import com.poly.model.Slide;
import com.poly.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SlideService {

    @Autowired
    private SlideRepository slideRepository;

    public Slide saveSlide(Slide slide) {
        return slideRepository.save(slide);
    }

    public void deleteSlideById(Integer id) {
        slideRepository.deleteById(id);
    }

    public Optional<Slide> getSlideById(Integer id) {
        return slideRepository.findById(id);
    }

    public List<Slide> getAllSlides() {
        return slideRepository.findAll();
    }
}
