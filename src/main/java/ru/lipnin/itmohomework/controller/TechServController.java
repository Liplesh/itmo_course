package ru.lipnin.itmohomework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.constants.DifficultyLevel;
import ru.lipnin.itmohomework.dto.TechServRequestDTO;
import ru.lipnin.itmohomework.dto.TechServResponseDTO;
import ru.lipnin.itmohomework.services.TechServService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/services")
public class TechServController {

    TechServService techServService;

    //Создать новую услугу
    @PostMapping
    public ResponseEntity<?> createTechService(@Valid @RequestBody TechServRequestDTO techServRequestDTO) {
        log.info("POST Creating Tech Serv Request: {}", techServRequestDTO);
        URI uri = URI.create("/api/v1/services?id=" +
                techServService.createTechServ(techServRequestDTO));
        return ResponseEntity.created(uri).build();
    }

    //Получить все услуги
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<TechServResponseDTO>> getTechServices() {
        log.info("GET all Tech Serv Requests");

        return ResponseEntity.ok(techServService.getTechServs());
    }

    //Получить услугу по id
    @GetMapping(produces = "application/json")
    public ResponseEntity<TechServResponseDTO> getTechServById(@RequestParam Long id) {
        log.info("GET Tech Serv Request with id = {}", id);
        return ResponseEntity.ok(techServService.getTechServById(id));
    }

    //Получить все услуги по переданной сложности
    @GetMapping(path = ("/all/{level}"),produces = "application/json")
    public ResponseEntity<List<TechServResponseDTO>> getTechServByDifficult(@PathVariable DifficultyLevel level) {
        log.info("GET Tech Serv Request with level = {}", level);
        return ResponseEntity.ok(techServService.getTechServsByDifficulty(level));
    }

    //Получить все забронированные услуги
    @GetMapping(path = ("/reserved"),produces = "application/json")
    public ResponseEntity<List<TechServResponseDTO>> getAllReservedTechServs() {
        log.info("GET All Reserved Tech Serv Requests");
        return ResponseEntity.ok(techServService.getReservedTechServs());
    }

    //Получить все свободные услуги до какой либо даты
    @GetMapping(path = ("/not_reserved_until/{date}"), produces = "application/json")
    public ResponseEntity<List<TechServResponseDTO>> getAllReservedTechServUntil(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date) {
        log.info("GET All Reserved Tech Serv Request with date = {}", date);
        return ResponseEntity.ok(techServService.getTechServsNotSignetUntil(date));
    }

    //Обновить услугу в бд
    @PutMapping("/{id}")
    public ResponseEntity<TechServResponseDTO> updateTechService(@NotNull @Positive @PathVariable Long id,
                                                @NotNull @Valid @RequestBody TechServRequestDTO techServRequestDTO) {
        log.info("PUT Tech Serv Request with id = {}", id);
        return new ResponseEntity<>(techServService.updateTechServ(id, techServRequestDTO), HttpStatus.OK);
    }

    //Удалить услугу
    @PutMapping("/delete")
    public ResponseEntity<Void> deleteTechService(@NotNull @Positive @RequestParam Long id) {
        log.info("DELETE Tech Serv Request with id = {}", id);
        techServService.deleteTechServ(id);
        return ResponseEntity.ok().build();
    }

    //Создать бронь
    @PutMapping("/reservation/reserve/{id}")
    public ResponseEntity<?> reserveTechServ(@NotNull @Positive @PathVariable Long id) {
        log.info("PUT Reserved Tech Serv Request with id = {}", id);
        techServService.reserveTechServ(id);
        URI uri = URI.create("/api/v1/services?id=" + id);
        return ResponseEntity.created(uri).build();
    }

    //Снять бронь
    @PutMapping("/reservation/cancel_reserve/{id}")
    public ResponseEntity<?> cancelReservedTechServ(@NotNull @Positive @PathVariable Long id) {
        log.info("PUT Cancel Reserved Tech Serv Request with id = {}", id);
        techServService.cancelReserveTechServ(id);
        URI uri = URI.create("/api/v1/services?id=" + id);
        return ResponseEntity.created(uri).build();
    }

    //Обновить время брони
    @PutMapping("/reservation/time_update")
    public ResponseEntity<TechServResponseDTO> updateSignedTime(@RequestBody @Valid TechServRequestDTO techServRequestDTO) {
        log.info("PUT Update signed time Request = {}", techServRequestDTO);
        return ResponseEntity.ok(techServService.updateSignetAt(techServRequestDTO));
    }



}
