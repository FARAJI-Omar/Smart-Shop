package com.smartshop.mapper;

import com.smartshop.dto.request.PromoCodeCreateDTO;
import com.smartshop.dto.response.PromoCodeResponseDTO;
import com.smartshop.entity.PromoCode;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {
    PromoCode toEntity(PromoCodeCreateDTO dto);
    PromoCodeResponseDTO toDTO(PromoCode promo);
    List<PromoCodeResponseDTO> toListDTO(List<PromoCode> promoCodes);

}
