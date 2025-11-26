package com.smartshop.service;

import com.smartshop.dto.request.ProductCreateDTO;
import com.smartshop.dto.request.ProductUpdateDTO;
import com.smartshop.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductCreateDTO dto);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO dto);
    void deleteProduct(Long id);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
}
