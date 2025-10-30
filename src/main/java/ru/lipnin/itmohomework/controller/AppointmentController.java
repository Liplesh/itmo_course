package ru.lipnin.itmohomework.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.dto.AppointmentEarningsRequestDTO;
import ru.lipnin.itmohomework.dto.AppointmentEarningsResponseDTO;
import ru.lipnin.itmohomework.dto.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.AppointmentResponseDTO;
import ru.lipnin.itmohomework.entity.Appointment;
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

    @PostMapping(path = "/create")
    public ResponseEntity<?> createAppointment(@Validated @RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        log.info("Create appointment request: {}", appointmentRequestDTO);
        URI uri = URI.create("/api/v1/appointment?id=" +
                appointmentService.createAppointment(appointmentRequestDTO));
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByName(@NotNull @NotBlank @RequestParam String name) {
        log.info("Get all appointments by name: {}", name);
        List<AppointmentResponseDTO> allAppointmentsByClientName = appointmentService.getAllAppointmentsByClientName(name);
        return ResponseEntity.ok(allAppointmentsByClientName);
    }

    @GetMapping(path = "/by_time")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByDate(
            @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        log.info("Get all appointments by date: {}", date);
        List<AppointmentResponseDTO> allAppointmentsByClientName = appointmentService.getAllAppointmentsByDate(date);
        return ResponseEntity.ok(allAppointmentsByClientName);
    }

    @PutMapping(path = "/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@NotNull @Positive @RequestParam Long id) {
        log.info("Cancel appointment: {}", id);
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @PutMapping(path = "/update")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @Validated @RequestBody AppointmentRequestDTO appointmentRequestDTO,
            @NotNull @Positive @RequestParam Long id) {
        log.info("Update appointment: {}", id);
        return ResponseEntity.ok( appointmentService.updateAppointment(id, appointmentRequestDTO));
    }

    //Нужно ли куда-то в другой контроллер, если ДТО отличается?
    //Правильно ли здесь использовать PUT
    @PutMapping(path = "/money")
    public ResponseEntity<AppointmentEarningsResponseDTO> getMoneyByPeriod(
            @RequestBody AppointmentEarningsRequestDTO appointmentEarningsRequestDTO) {
        log.info("Get money by period: {}", appointmentEarningsRequestDTO);
        return ResponseEntity.ok(appointmentService.findAllEarnedMoneyByPeriod(appointmentEarningsRequestDTO));
    }

}
