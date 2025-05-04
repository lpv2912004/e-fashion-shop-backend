package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConflictCheckDTO {
    private List<Integer> variantIds;
    private Date startDate;
    private Date endDate;

    // Getters và Setters
}
