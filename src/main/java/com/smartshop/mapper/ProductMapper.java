package com.smartshop.mapper;

import com.smartshop.dto.request.ProductCreateDTO;
import com.smartshop.dto.request.ProductUpdateDTO;
import com.smartshop.dto.response.ProductResponseDTO;
import com.smartshop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductCreateDTO dto);
    void updateEntity(@MappingTarget Product product, ProductUpdateDTO dto);
    ProductResponseDTO toDTO(Product product);
    List<ProductResponseDTO> toListDTO(List<Product> products);
}
