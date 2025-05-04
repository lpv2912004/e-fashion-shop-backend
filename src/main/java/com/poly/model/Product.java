package com.poly.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name_prod;
    private String image_prod;
    private Boolean status_prod;
    private Integer sum_quantity;
    private Integer created_by;
    private Integer updated_by;
    private String description;	

    @ManyToOne
    @JoinColumn(name = "id_cate")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_brand")
    private Brand brand;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_date = new Date();

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<VariantProduct> variantProds;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Like> likes;

}
