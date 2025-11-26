package com.smartshop.mapper;

import com.smartshop.dto.request.ClientCreateDTO;
import com.smartshop.dto.request.ClientUpdateDTO;
import com.smartshop.dto.response.ClientResponseDTO;
import com.smartshop.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientCreateDTO dto);
    void updateEntity(@MappingTarget Client client, ClientUpdateDTO dto);
    ClientResponseDTO toDTO(Client client);
    List<ClientResponseDTO> toListDTO(List<Client> clients);
}
