package ru.lipnin.itmohomework.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwnAppointmentDto;
import ru.lipnin.itmohomework.entity.Appointment;

@Service
public class DwhMapper {

    public DwnAppointmentDto mapToDto(Appointment appointment) {
        return new DwnAppointmentDto(
                appointment.getId(),
                appointment.getClientName(),
                appointment.getClientPhone(),
                appointment.getAppointmentTime(),
                appointment.getAppointmentClose(),
                appointment.getPrice(),
                appointment.getPriceNote(),
                appointment.getService().getId(),
                appointment.getUser().getId()
        );
    }

}
