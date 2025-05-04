package com.poly.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VariantProds")
public class VariantProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    private Double price;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Temporal(TemporalType.DATE)
    private Date created_date = new Date();

    @Temporal(TemporalType.DATE)
    private Date updated_date = new Date();

    private String image_variant;

    @Column(name = "status_VP", columnDefinition = "int default 1")
    private int status_VP = 1;
//    private String description;

//    @ManyToOne
//    @JoinColumn(name = "id_flashSale")
//    @JsonIgnoreProperties({"variantProds", "user", "activitySales"}) // Bỏ qua các thuộc tính gây vòng lặp
//    private FlashSale flashSale;

    @ManyToOne
    @JoinColumn(name = "id_product")
    @JsonIgnoreProperties({"variantProds"}) // Bỏ qua các thuộc tính gây vòng lặp
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_color")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "id_size")
    private Size size;

    @JsonIgnore
    @OneToMany(mappedBy = "variantProd")
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "variantProd")
    private List<CartDetail> cartDetails;
    
    @JsonIgnore
    @OneToMany(mappedBy = "variantProduct")
    private List<ActivitySale> activitySales;
}
