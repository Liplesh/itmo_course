package ru.lipnin.itmohomework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.constants.Category;
import ru.lipnin.itmohomework.dto.ServiceRequestDTO;
import ru.lipnin.itmohomework.dto.ServiceResponseDTO;
import ru.lipnin.itmohomework.entity.BeautyService;
import ru.lipnin.itmohomework.mapper.BeautyServiceMapper;
import ru.lipnin.itmohomework.repository.BeautyServiceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BeautyServService {

    private final BeautyServiceRepository servRepository;
    private final BeautyServiceMapper beautyServiceMapper;

    //Создать услуг
    public Long createService(ServiceRequestDTO serviceRequestDTO) {
        BeautyService beautyService = beautyServiceMapper.mapToEntity(serviceRequestDTO);

        servRepository.save(beautyService);
        return beautyService.getId();
    }

    public ServiceResponseDTO getServiceById(Long id) {
        BeautyService beautyService = servRepository.findByIdAndRemovedFalse(id).get();
//        BeautyService beautyService = servRepository.findById(id).get();
        //TODO ошибку выкинуть
        return beautyServiceMapper.mapToDTO(beautyService);
    }

    //Получить все услуги по категории
    public List<ServiceResponseDTO> getAllServiceByCategory(Category category) {
        List<BeautyService> beautyServiceByCategory = servRepository.findAllBeautyServiceByCategory(category);
        return beautyServiceByCategory.stream().map(beautyServiceMapper::mapToDTO).collect(Collectors.toList());
    }

    //Получить все свободные услуги
    public List<ServiceResponseDTO> getAllNotReservedServiceByDate(LocalDateTime appointmentDate) {
        List<BeautyService> allNotReservedServicesByTime = servRepository.findAllNotReservedServicesByTime(appointmentDate);
        return allNotReservedServicesByTime.stream().map(beautyServiceMapper::mapToDTO).collect(Collectors.toList());
    }

}
