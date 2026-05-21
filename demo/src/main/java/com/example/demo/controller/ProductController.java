package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @RequestBody ProductRequest request
    ) {

        ProductResponse response = productService.create(request);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(200)
                        .message("Created product successful!")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {

        List<ProductResponse> response = productService.getAll();

        return ResponseEntity.ok(
                ApiResponse.<List<ProductResponse>>builder()
                        .status(200)
                        .message("Get list product successful!")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(
            @PathVariable UUID id
    ) {

        ProductResponse response = productService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(200)
                        .message("Get product successful!")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request
    ) {

        ProductResponse response =
                productService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .status(200)
                        .message("Updated product successful!")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable UUID id
    ) {

        productService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("Delete product successful!")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategoryId(
            @PathVariable UUID categoryId
    ) {

        List<ProductResponse> response =
                productService.getByCategoryId(categoryId);

        return ResponseEntity.ok(
                ApiResponse.<List<ProductResponse>>builder()
                        .status(200)
                        .message("Get product by category successful!")
                        .data(response)
                        .build()
        );
    }
}