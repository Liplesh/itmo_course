package ru.lipnin.itmohomework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lipnin.itmohomework.dto.discont.DiscountRequestDTO;
import ru.lipnin.itmohomework.services.DiscountService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/discount")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody @Validated DiscountRequestDTO discountRequestDTO) {
        log.info("Create discount");
        return ResponseEntity.ok(discountService.createDiscount(discountRequestDTO));
    }

}
