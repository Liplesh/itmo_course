package ru.lipnin.itmohomework.services.notification;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageService {

    public String getSorryMessage(ApplicationUser user) {
        return """
        Уважаемый(ая), %s!

        Приносим свои извинения за доставленные неудобства.
        Дарим Вам скидку 15%% на следующую услугу!

        С уважением,
        Ваша служба поддержки
        """.formatted(user.getUsername());
    }

    public String getConfirmMessage(Appointment appointment) {
        String username = appointment.getUser().getUsername();
        String name = appointment.getService().getName();
        LocalDateTime appointmentTime = appointment.getAppointmentTime();
        String dateString = appointmentTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timeString = appointmentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        return """
        Уважаемый(ая), %s!

        Рады сообщить, что ваше бронирование успешно подтверждено.
        Детали бронирования:
        ✅ Услуга: %s
        📅 Дата: %s
        🕒 Время: %s

        С уважением,
        Ваша служба поддержки
        """.formatted(username, name, dateString, timeString);
    }

    public String getUpdatedMessage(Appointment appointment) {
        String username = appointment.getUser().getUsername();
        String name = appointment.getService().getName();
        LocalDateTime appointmentTime = appointment.getAppointmentTime();
        String dateString = appointmentTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String timeString = appointmentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        return """
        Уважаемый(ая), %s!

        Ваша бронь изменена.
        Детали бронирования:
        ✅ Услуга: %s
        📅 Дата: %s
        🕒 Время: %s

        С уважением,
        Ваша служба поддержки
        """.formatted(username, name, dateString, timeString);
    }
}
