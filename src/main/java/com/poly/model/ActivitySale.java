package com.poly.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ActivitySales")
public class ActivitySale {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration_date;

    private Integer discount_percent;

    @ManyToOne
    @JoinColumn(name = "id_flashSale")
    private FlashSale flashSale;
    
    @ManyToOne
    @JoinColumn(name = "id_variantproduct")
    private VariantProduct variantProduct;
}
