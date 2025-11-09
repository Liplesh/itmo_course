package ru.lipnin.itmohomework.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.service.BeautyServiceRequestDTO;
import ru.lipnin.itmohomework.dto.service.BeautyServiceResponseDTO;
import ru.lipnin.itmohomework.entity.BeautyService;

@Service
public class BeautyServiceMapper {

    public BeautyService mapToEntity(BeautyServiceRequestDTO serviceRequestDTO) {
        return BeautyService.builder()
                .name(serviceRequestDTO.name())
                .description(serviceRequestDTO.description())
                .duration(serviceRequestDTO.duration())
                .price(serviceRequestDTO.price())
                .category(serviceRequestDTO.category())
                .build();
    }

    public BeautyServiceResponseDTO mapToDTO(BeautyService beautyService) {
        return new BeautyServiceResponseDTO(
                beautyService.getName(),
                beautyService.getDescription(),
                beautyService.getDuration(),
                beautyService.getPrice(),
                beautyService.getCategory()
        );
    }
}
