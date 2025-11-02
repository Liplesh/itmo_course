package ru.lipnin.itmohomework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.constants.Category;
import ru.lipnin.itmohomework.dto.service.ServiceRequestDTO;
import ru.lipnin.itmohomework.dto.service.ServiceResponseDTO;
import ru.lipnin.itmohomework.services.BeautyServService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/service")
public class BeautyServiceController {
    private final BeautyServService beautyService;

    @PostMapping()
    public ResponseEntity<?> createTechService(@Valid @RequestBody ServiceRequestDTO serviceRequestDTO) {
        log.info("POST Creating Tech Serv Request: {}", serviceRequestDTO);
        URI uri = URI.create("/api/v1/service?id=" +
                beautyService.createService(serviceRequestDTO));
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<ServiceResponseDTO> getServiceById(@NotNull @Positive @RequestParam Long id) {
        log.info("GET Service by ID: {}", id);
        ServiceResponseDTO serviceResponseDTO = beautyService.getServiceById(id);
        return ResponseEntity.ok(serviceResponseDTO);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ServiceResponseDTO>> getAllServicesByCategory(@NotNull @RequestParam Category category) {
        log.info("GET Service by Category: {}", category);
        List<ServiceResponseDTO> allServiceByCategory = beautyService.getAllServiceByCategory(category);
        return ResponseEntity.ok(allServiceByCategory);
    }

    @GetMapping(path = "/not_reserved")
    public ResponseEntity<List<ServiceResponseDTO>> getAllNotReservedServicesByDate(
            @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        log.info("GET Service By Date: {}", date);
        List<ServiceResponseDTO> allServiceByCategory = beautyService.getAllNotReservedServiceByDate(date);
        return ResponseEntity.ok(allServiceByCategory);
    }

}
