package ru.lipnin.itmohomework.dto.discont;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.lipnin.itmohomework.constants.DiscountType;

import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DiscountRequestDTO(
        @NotNull
        @NotBlank
        String description,

        @NotNull
        @Positive
        Float value,

        @NotNull
        @Future
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startDate,

        @NotNull
        @Future
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endDate,

        @NotNull
        DiscountType discountType,

        //Правильно ли сделать так,
        //если я подразумеваю, что в зависимости от Type будут приходить разные id, т.е.

        // PERSON - Будет лист id людей, которым предоставлена скидка
        List<Long> personId,
        // SERVICE - Будет лист id услуг, на которые скидка
        List<Long> serviceId,
        // APPOINTMENT - Будет лист id броней, на которые скидка
        List<Long> appointmentId

) {
}
