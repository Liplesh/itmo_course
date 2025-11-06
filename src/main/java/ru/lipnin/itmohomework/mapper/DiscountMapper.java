package ru.lipnin.itmohomework.mapper;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.discont.DiscountRequestDTO;
import ru.lipnin.itmohomework.entity.Discount;

@Service
public class DiscountMapper {

    public Discount mapToEntity(DiscountRequestDTO discountRequestDTO){
        return Discount.builder()
                .description(discountRequestDTO.description())
                .value(discountRequestDTO.value())
                .type(discountRequestDTO.discountType())
                .startDate(discountRequestDTO.startDate())
                .endDate(discountRequestDTO.endDate())
                .build();
    }
}
