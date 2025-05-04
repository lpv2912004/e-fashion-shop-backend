package com.poly.service;

import com.poly.model.Size;
import com.poly.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    public Optional<Size> findById(Integer id) {
        return sizeRepository.findById(id);
    }

    public Size save(Size size) {
        return sizeRepository.save(size);
    }

    public void deleteById(Integer id) {
        sizeRepository.deleteById(id);
    }
}
