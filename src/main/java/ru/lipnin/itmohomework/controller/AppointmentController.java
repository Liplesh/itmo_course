package ru.lipnin.itmohomework.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.dto.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.AppointmentResponseDTO;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.services.AppointmentService;

import java.net.URI;
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

}
