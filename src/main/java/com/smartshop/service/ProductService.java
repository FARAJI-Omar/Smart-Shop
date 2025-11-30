package com.smartshop.service;

import com.smartshop.dto.request.ProductCreateDTO;
import com.smartshop.dto.request.ProductUpdateDTO;
import com.smartshop.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductCreateDTO dto);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO dto);
    void deleteProduct(Long id);
    Page<ProductResponseDTO> getAllProducts(String name, Double minPrice, Double maxPrice, int page, int size);
    ProductResponseDTO getProductById(Long id);
}
