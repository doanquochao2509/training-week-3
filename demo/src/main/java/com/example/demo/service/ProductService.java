package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    List<ProductResponse> getAll();

    ProductResponse getById(UUID id);

    ProductResponse update(UUID id, ProductRequest request);

    void delete(UUID id);

    List<ProductResponse> getByCategoryId(UUID categoryId);
}