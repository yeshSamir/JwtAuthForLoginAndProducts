package com.jwtauth.controller;

import com.jwtauth.entity.Product;
import com.jwtauth.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class RestControllerForJwt {

    @Autowired
    ProductRepository productRepository;


    @GetMapping
    public List<Product> productList (){
        return productRepository.findAll();
    }

}
