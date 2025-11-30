package com.smartshop.controller;

import com.smartshop.dto.request.ProductCreateDTO;
import com.smartshop.dto.request.ProductUpdateDTO;
import com.smartshop.dto.response.ProductResponseDTO;
import com.smartshop.service.ProductService;
import com.smartshop.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductCreateDTO dto, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return productService.createProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO dto, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request)) {
            throw new SecurityException("Admin access required");
        }
        productService.deleteProduct(id);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request) && !SessionUtil.isClient(request)) {
            throw new SecurityException("Please login.");
        }
        return ResponseEntity.ok(productService.getAllProducts(name, minPrice, maxPrice, page, size));
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id, HttpServletRequest request) {
        if (!SessionUtil.isAdmin(request) || !SessionUtil.isClient(request)) {
            throw new SecurityException("Please login.");
        }
        return productService.getProductById(id);
    }
}
