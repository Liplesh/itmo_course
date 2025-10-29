package ru.lipnin.itmohomework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.constants.Status;
import ru.lipnin.itmohomework.dto.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.AppointmentResponseDTO;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.entity.BeautyService;
import ru.lipnin.itmohomework.mapper.AppointmentMapper;
import ru.lipnin.itmohomework.repository.AppointmentRepository;
import ru.lipnin.itmohomework.repository.BeautyServiceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final BeautyServiceRepository beautyServiceRepository;
    private final AppointmentMapper mapper;

    //Создать бронь
    public Long createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        BeautyService beautyService = beautyServiceRepository.findByIdAndRemovedFalse(appointmentRequestDTO.serviceId()).get();
        //TODO выкинуть исключение

        boolean serviceReserved = beautyServiceRepository.isServiceReserved(beautyService.getId(), appointmentRequestDTO.appointmentTime());
        if (serviceReserved) {
            log.info("Уже зарезервирована услуга {}", beautyService.getName());
            //TODO выкинуть исключение, что забронированно уже
        }

        Appointment appointment = mapper.mapToEntity(appointmentRequestDTO);
        appointment.setStatus(Status.CREATED);
        appointment.setService(beautyService);
        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    //Получить все брони клиента (Пока предполагаем, что имя уникально, потом id сделаю)
    public List<AppointmentResponseDTO> getAllAppointmentsByClientName(String clientName) {
        List<Appointment> allAppointmentByClientName = appointmentRepository.findAllAppointmentByClientNameAndRemovedFalse(clientName);
        return allAppointmentByClientName.stream().map(mapper::mapToDTO).collect(Collectors.toList());
    }

    //Снять бронь
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId).get();
        //TODO ошибка если нет

        appointment.setStatus(Status.CANCELLED);
        appointment.setAppointmentClose(LocalDateTime.now());
        //Как тут сделать правильно логику, если далее зануляю ссылку и в мапере упадет нпе?
        AppointmentResponseDTO appointmentResponseDTO = mapper.mapToDTO(appointment);
        appointment.setService(null);
        appointmentRepository.save(appointment);
        return appointmentResponseDTO;
    }

    //Обновить время брони
    public AppointmentResponseDTO updateAppointment(Long appointmentId, AppointmentRequestDTO appointmentRequestDTO) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId).get();
        //TODO ошибка если нет

        //Здесь лучше сделать еще одно дто? Я просто хочу обновить время записи,
        // а передаю целиком все поля, что может запутать
        appointment.setAppointmentTime(appointmentRequestDTO.appointmentTime());
        appointmentRepository.save(appointment);
        return mapper.mapToDTO(appointment);
    }

}
