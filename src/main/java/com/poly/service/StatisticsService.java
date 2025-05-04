package com.poly.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.DailyOrderCountDTO;
import com.poly.dto.DailyRevenueDTO;
import com.poly.dto.FlashSaleStatisticsDTO;
import com.poly.dto.ProductInventoryDTO;
import com.poly.dto.ProductsDTo;
import com.poly.dto.UserStatisticsDTO;
import com.poly.dto.VoucherStatisticsDTO;
import com.poly.repository.OrderDetailRepository;



@Service
public class StatisticsService {
    @Autowired
    private OrderDetailRepository orderDetailsRepository;

    public List<ProductsDTo> getSoldProductsBetweenDates(LocalDateTime fromDateTime, LocalDateTime toTimestamp) {
        // Chuyển đổi LocalDate thành Timestamp
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());

        return orderDetailsRepository.findSoldProductsBetweenDates(fromDateTime, toTimestamp);
    }

    public double getRevenueBetweenDates(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());

        Double revenue = orderDetailsRepository.findRevenueBetweenDates(fromDateTime, toDateTime);
        return (revenue != null) ? revenue : 0.0;  // Handle null return by providing default value 0.0
    }

    
    public double getRevenueBetweenDates2(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
//      Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//      Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());

      Double revenue = orderDetailsRepository.findTotalCashBetweenDates(fromDateTime, toDateTime);
      return (revenue != null) ? revenue : 0.0;  // Handle null return by providing default value 0.0
  }

    
    
    
    public List<ProductInventoryDTO> getInventory() {
        return orderDetailsRepository.findInventory();
    }
    
    
    
    public List<UserStatisticsDTO> getUserStatistics(LocalDateTime fromTimestamp, LocalDateTime toTimestamp) {
//        Timestamp fromTimestamp = Timestamp.valueOf(from.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(to.atStartOfDay());
        return orderDetailsRepository.findUserStatistics(fromTimestamp, toTimestamp);
    }

    public List<VoucherStatisticsDTO> getVoucherStatistics(LocalDateTime fromTimestamp, LocalDateTime toTimestamp) {
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());
        return orderDetailsRepository.findVoucherStatistics(fromTimestamp,toTimestamp);
    }

    public List<FlashSaleStatisticsDTO> getFlashSaleStatistics(LocalDateTime fromTimestamp, LocalDateTime toTimestamp) {
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());
        return orderDetailsRepository.findFlashSaleStatistics(fromTimestamp, toTimestamp);
    }
    public List<DailyRevenueDTO> getDailyRevenue(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());
        return orderDetailsRepository.findDailyRevenueBetweenDates(fromDateTime, toDateTime);
    }

    public List<DailyOrderCountDTO> getDailyOrderCount(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
//        Timestamp fromTimestamp = Timestamp.valueOf(fromDateTime.atStartOfDay());
//        Timestamp toTimestamp = Timestamp.valueOf(toDateTime.atStartOfDay());
        return orderDetailsRepository.findDailyOrderCountBetweenDates(fromDateTime, toDateTime);
    }

}

