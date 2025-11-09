package ru.lipnin.itmohomework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.constants.Category;
import ru.lipnin.itmohomework.dto.service.BeautyServiceRequestDTO;
import ru.lipnin.itmohomework.dto.service.BeautyServiceResponseDTO;
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

    @Secured({"ROLE_ADMIN", "ROLE_SUPER_USER"})
    @PostMapping()
    public ResponseEntity<?> createTechService(@Valid @RequestBody BeautyServiceRequestDTO serviceRequestDTO) {
        log.info("POST Creating Tech Serv Request: {}", serviceRequestDTO);
        URI uri = URI.create("/api/v1/service?id=" +
                beautyService.createService(serviceRequestDTO));
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<BeautyServiceResponseDTO> getServiceById(@NotNull @Positive @RequestParam Long id) {
        log.info("GET Service by ID: {}", id);
        BeautyServiceResponseDTO serviceResponseDTO = beautyService.getServiceById(id);
        return ResponseEntity.ok(serviceResponseDTO);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<BeautyServiceResponseDTO>> getAllServicesByCategory(@NotNull @RequestParam Category category) {
        log.info("GET Service by Category: {}", category);
        List<BeautyServiceResponseDTO> allServiceByCategory = beautyService.getAllServiceByCategory(category);
        return ResponseEntity.ok(allServiceByCategory);
    }

    @GetMapping(path = "/not_reserved")
    public ResponseEntity<List<BeautyServiceResponseDTO>> getAllNotReservedServicesByDate(
            @NotNull @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        log.info("GET Service By Date: {}", date);
        List<BeautyServiceResponseDTO> allServiceByCategory = beautyService.getAllNotReservedServiceByDate(date);
        return ResponseEntity.ok(allServiceByCategory);
    }

}
