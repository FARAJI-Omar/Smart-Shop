package com.smartshop.service.impl;

import com.smartshop.dto.request.ProductCreateDTO;
import com.smartshop.dto.request.ProductUpdateDTO;
import com.smartshop.dto.response.ProductResponseDTO;
import com.smartshop.entity.Product;
import com.smartshop.mapper.ProductMapper;
import com.smartshop.repository.ProductRepository;
import com.smartshop.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getUnitPrice() != null) {
            product.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getAvailableStock() != null) {
            product.setAvailableStock(dto.getAvailableStock());
        }
        
        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        if (product.getOrderItems() != null && !product.getOrderItems().isEmpty()) {
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            productRepository.deleteById(id);
        }
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productMapper.toListDTO(productRepository.findByDeletedFalse());
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        if (product.getDeleted()) {
            throw new EntityNotFoundException("Product not found");
        }
        
        return productMapper.toDTO(product);
    }
}
