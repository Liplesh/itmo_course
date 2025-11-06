package ru.lipnin.itmohomework.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.dto.appointment.AppointmentEarningsResponseDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentResponseDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentUpdateRequestDTO;
import ru.lipnin.itmohomework.services.AppointmentService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping()
    public ResponseEntity<?> createAppointment(@Validated @RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        log.info("Create appointment request: {}", appointmentRequestDTO);
        URI uri = URI.create("/api/v1/appointment?id=" +
                appointmentService.createAppointment(appointmentRequestDTO));
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        log.info("Get all appointments by user");
        List<AppointmentResponseDTO> allAppointmentsByClientName = appointmentService.getAllAppointmentsByClient();
        return ResponseEntity.ok(allAppointmentsByClientName);
    }

    @PutMapping(path = "/cancellation")
    public ResponseEntity<?> cancelAppointment(@NotNull @Positive @RequestParam Long id) {
        log.info("Cancel appointment: {}", id);
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @Validated @RequestBody AppointmentUpdateRequestDTO appointmentRequestDTO) {
        log.info("Update appointment: {}", appointmentRequestDTO);
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentRequestDTO));
    }

    //Подтвердить оператором бронирование - что бы дальше можно было взять в работу
    //TODO возможно следующим шагом придумать сущность мастеров, которые будут присваиваться после этого этапа
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_USER"})
    @PutMapping(("/admin/confirmation"))
    public ResponseEntity<Void> confirmAppointment(@NotNull @Positive @RequestParam Long id){
        log.info("Confirm appointment: {}", id);
        appointmentService.confirmAppointment(id);
        return ResponseEntity.ok().build();
    }

    //Закрыть бронь опертором, по выполнению, присвоить выручку с учетом скидки
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_USER"})
    @PutMapping(("/admin/completion"))
    public ResponseEntity<Void> completeAppointment(@NotNull @Positive @RequestParam Long id){
        log.info("Complete appointment: {}", id);
        appointmentService.completeAppointment(id);
        return ResponseEntity.ok().build();
    }

    //Отмена оператором любой брони
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_USER"})
    @PutMapping(path = "/admin/cancellation")
    public ResponseEntity<?> forceCancelAppointment(@NotNull @Positive @RequestParam Long id) {
        log.info("Cancel admin appointment: {}", id);
        appointmentService.forceCancelAppointment(id);
        return ResponseEntity.ok().build();
    }

    //Изменение времени любой брони оператором
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_USER"})
    @PutMapping("/admin")
    public ResponseEntity<AppointmentResponseDTO> forceUpdateAppointment(
            @Validated @RequestBody AppointmentUpdateRequestDTO appointmentRequestDTO) {
        log.info("Update admin appointment: {}", appointmentRequestDTO);
        return ResponseEntity.ok(appointmentService.forceUpdateAppointment(appointmentRequestDTO));
    }

    //Получить все брони по дате
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_USER"})
    @GetMapping(path = "/admin/by_time")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByDate(
            @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        log.info("Get all appointments by date: {}", date);
        List<AppointmentResponseDTO> allAppointments = appointmentService.getAllAppointmentsByDate(date);
        return ResponseEntity.ok(allAppointments);
    }

    //Получить расчет прибыли за период
    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/admin/money")
    public ResponseEntity<AppointmentEarningsResponseDTO> getMoneyByPeriod(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime end) {
        log.info("Get money by period: {}", start);
        log.info("Get money by period: {}", end);
        return ResponseEntity.ok(appointmentService.findAllEarnedMoneyByPeriod(start, end));
    }
}
