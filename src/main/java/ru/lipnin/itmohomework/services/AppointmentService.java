package ru.lipnin.itmohomework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.constants.Status;
import ru.lipnin.itmohomework.dto.AppointmentEarningsRequestDTO;
import ru.lipnin.itmohomework.dto.AppointmentEarningsResponseDTO;
import ru.lipnin.itmohomework.dto.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.AppointmentResponseDTO;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.entity.BeautyService;
import ru.lipnin.itmohomework.exception.AppointmentException;
import ru.lipnin.itmohomework.exception.BeautyServiceException;
import ru.lipnin.itmohomework.mapper.AppointmentMapper;
import ru.lipnin.itmohomework.repository.AppointmentRepository;
import ru.lipnin.itmohomework.repository.BeautyServiceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        BeautyService beautyService = beautyServiceRepository
                .findByIdAndRemovedFalse(appointmentRequestDTO.serviceId())
                .orElseThrow(() -> new BeautyServiceException(HttpStatus.NOT_FOUND, "Услуга не найдена"));

        boolean serviceReserved = beautyServiceRepository.isServiceReserved(beautyService.getId(), appointmentRequestDTO.appointmentTime());
        if (serviceReserved) {
            log.info("Уже зарезервирована услуга {}", beautyService.getName());
            throw new AppointmentException(HttpStatus.CONFLICT, "Услуга уже забронирована");
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
        if (allAppointmentByClientName.isEmpty()) {
            throw new AppointmentException(HttpStatus.NOT_FOUND, "Записи не найдены");
        }
        return allAppointmentByClientName.stream().map(mapper::mapToDTO).collect(Collectors.toList());
    }

    //Получить все забронированные услуги по времени
    public List<AppointmentResponseDTO> getAllAppointmentsByDate(LocalDateTime appointmentDate) {
        List<Appointment> appointments = appointmentRepository
                .findAllAppointmentByAppointmentTimeAndRemovedFalse(appointmentDate);

        if (appointments.isEmpty()) {
            throw new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена");
        }

        return appointments.stream().map(mapper::mapToDTO).collect(Collectors.toList());
    }

    //Снять бронь
    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

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
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        //Здесь лучше сделать еще одно дто? Я просто хочу обновить время записи,
        // а передаю целиком все поля, что может запутать
        appointment.setAppointmentTime(appointmentRequestDTO.appointmentTime());
        appointmentRepository.save(appointment);
        return mapper.mapToDTO(appointment);
    }

    //Получить выручку по переданной дате
    public AppointmentEarningsResponseDTO findAllEarnedMoneyByPeriod(AppointmentEarningsRequestDTO earningsRequestDTO) {
        LocalDateTime from = earningsRequestDTO.from();
        LocalDateTime to = earningsRequestDTO.to() == null ? LocalDateTime.now() : earningsRequestDTO.to();

        BigDecimal allEarnedMoneyByPeriod = appointmentRepository.findAllEarnedMoneyByPeriod(from, to);
        if (allEarnedMoneyByPeriod == null) {
            allEarnedMoneyByPeriod = BigDecimal.ZERO;
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (from == null) {
            stringBuilder.append("по ").append(to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        } else {
            stringBuilder.append("с ").append(from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                    .append(" по ").append(to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        return new AppointmentEarningsResponseDTO(stringBuilder.toString(), allEarnedMoneyByPeriod);
    }

}
