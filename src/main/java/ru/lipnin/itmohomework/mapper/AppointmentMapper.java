package ru.lipnin.itmohomework.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.appointment.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentResponseDTO;
import ru.lipnin.itmohomework.entity.Appointment;

@Service
public class AppointmentMapper {

    public Appointment mapToEntity(AppointmentRequestDTO appointmentRequestDTO) {
        return Appointment.builder()
                .clientName(appointmentRequestDTO.clientName())
                .clientPhone(appointmentRequestDTO.clientPhone())
                .appointmentTime(appointmentRequestDTO.appointmentTime())
                .note(appointmentRequestDTO.note())
                .build();
    }

    public AppointmentResponseDTO mapToDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getClientName(),
                appointment.getClientPhone(),
                appointment.getNote(),
                appointment.getAppointmentTime(),
                appointment.getCreatedAt(),
                appointment.getAppointmentClose(),
                appointment.getService().getName()
        );
    }
}
