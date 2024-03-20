package com.microservicesproject.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservicesproject.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
