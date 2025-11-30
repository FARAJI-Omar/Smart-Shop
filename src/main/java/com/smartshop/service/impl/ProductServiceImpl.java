package com.smartshop.service.impl;

import com.smartshop.dto.request.ProductCreateDTO;
import com.smartshop.dto.request.ProductUpdateDTO;
import com.smartshop.dto.response.ProductResponseDTO;
import com.smartshop.entity.Product;
import com.smartshop.mapper.ProductMapper;
import com.smartshop.repository.ProductRepository;
import com.smartshop.service.ProductService;
import com.smartshop.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<ProductResponseDTO> getAllProducts(String name, Double minPrice, Double maxPrice, int page, int size) {
        // Build specification - always exclude deleted products
        Specification<Product> spec = Specification.where(ProductSpecification.isNotDeleted());

        // Add filters if provided
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasName(name));
        }

        if (minPrice != null) {
            spec = spec.and(ProductSpecification.hasMinPrice(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.hasMaxPrice(maxPrice));
        }

        // Create pageable
        Pageable pageable = PageRequest.of(page, size);

        // Query with specification
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(productMapper::toDTO);
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
