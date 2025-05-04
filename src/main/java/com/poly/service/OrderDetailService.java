package com.poly.service;

import com.poly.dto.UserStatisticsDTO;
import com.poly.model.OrderDetail;
import com.poly.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public Optional<OrderDetail> findById(Integer id) {
        return orderDetailRepository.findById(id);
    }

    public List<OrderDetail> findByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
    
    public List<UserStatisticsDTO> getUserStatistics(LocalDateTime fromTimestamp, LocalDateTime toTimestamp) {
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());
        return orderDetailRepository.findUserStatistics(fromTimestamp, toTimestamp);
    }
}
