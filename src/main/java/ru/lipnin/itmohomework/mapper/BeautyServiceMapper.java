package ru.lipnin.itmohomework.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.ServiceRequestDTO;
import ru.lipnin.itmohomework.dto.ServiceResponseDTO;
import ru.lipnin.itmohomework.entity.BeautyService;

@Service
public class BeautyServiceMapper {

    public BeautyService mapToEntity(ServiceRequestDTO serviceRequestDTO) {
        return BeautyService.builder()
                .name(serviceRequestDTO.name())
                .description(serviceRequestDTO.description())
                .duration(serviceRequestDTO.duration())
                .price(serviceRequestDTO.price())
                .category(serviceRequestDTO.category())
                .build();
    }

    public ServiceResponseDTO mapToDTO(BeautyService beautyService) {
        return new ServiceResponseDTO(
                beautyService.getName(),
                beautyService.getDescription(),
                beautyService.getDuration(),
                beautyService.getPrice(),
                beautyService.getCategory()
        );
    }
}
