package com.jwtauth.entity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 128)
    @Length(min = 3,max = 128)
    private String name;
    private float price;

}
