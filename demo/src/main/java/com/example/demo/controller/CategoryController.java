package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CategoryRequest;
import com.example.demo.dto.CategoryResponse;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @Valid @RequestBody CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.create(request);

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .status(200)
                        .message("Created category successful!")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {

        List<CategoryResponse> response =
                categoryService.getAll();

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .status(200)
                        .message("Get list category successful!")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(
            @PathVariable UUID id
    ) {

        CategoryResponse response =
                categoryService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .status(200)
                        .message("Get category successful!")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .status(200)
                        .message("Update category successful!")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable UUID id
    ) {

        categoryService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(200)
                        .message("Delete category successful!")
                        .data(null)
                        .build()
        );
    }
}